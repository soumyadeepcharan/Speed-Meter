package com.sdcgroup.speedmeter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animate
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import kotlin.math.abs
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Velocity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.sdcgroup.speedmeter.data.DataUnit
import com.sdcgroup.speedmeter.data.SpeedUnit
import com.sdcgroup.speedmeter.data.ThemeMode
import com.sdcgroup.speedmeter.data.dataUnitFlow
import com.sdcgroup.speedmeter.data.indicatorEnabledFlow
import com.sdcgroup.speedmeter.data.quitRequestedFlow
import com.sdcgroup.speedmeter.data.setDataUnit
import com.sdcgroup.speedmeter.data.setIndicatorEnabled
import com.sdcgroup.speedmeter.data.setQuitRequested
import com.sdcgroup.speedmeter.data.setSpeedUnit
import com.sdcgroup.speedmeter.data.setThemeMode
import com.sdcgroup.speedmeter.data.speedUnitFlow
import com.sdcgroup.speedmeter.data.themeModeFlow
import com.sdcgroup.speedmeter.notification.NotificationHelper
import com.sdcgroup.speedmeter.notification.NotificationHelper.Companion.NOTIFICATION_ID
import com.sdcgroup.speedmeter.service.SpeedMonitorService
import com.sdcgroup.speedmeter.ui.MyComposeApplicationTheme
import com.sdcgroup.speedmeter.notification.SpeedMeterSettingsScreen
import com.sdcgroup.speedmeter.ui.SpeedMeterTopAppBar
import com.sdcgroup.speedmeter.ui.helpSheetNavBarColor
import android.app.NotificationManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startServiceIfEnabled()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        NotificationHelper(this).createNotificationChannel()

        lifecycleScope.launch {
            val quitRequested = quitRequestedFlow(this@MainActivity).first()
            if (quitRequested) {
                setQuitRequested(this@MainActivity, false)
                return@launch
            }

            val isEnabled = indicatorEnabledFlow(this@MainActivity).first()
            if (!isEnabled) return@launch

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    startServiceIfEnabled()
                } else {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                startServiceIfEnabled()
            }
        }

        setContent {
            SpeedMeterApp()
        }
    }

    private fun startServiceIfEnabled() {
        lifecycleScope.launch {
            val isEnabled = indicatorEnabledFlow(this@MainActivity).first()
            if (isEnabled) {
                val intent = Intent(this@MainActivity, SpeedMonitorService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }
        }
    }
}

fun quitApp(activity: ComponentActivity) {
    runBlocking {
        setIndicatorEnabled(activity, false)
        setQuitRequested(activity, true)
    }

    val quitIntent = Intent(activity, SpeedMonitorService::class.java).apply {
        action = SpeedMonitorService.ACTION_QUIT
    }
    activity.startService(quitIntent)

    val notificationManager =
        activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(NOTIFICATION_ID)

    activity.finishAffinity()
    Process.killProcess(Process.myPid())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedMeterApp() {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val scope = rememberCoroutineScope()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val dragRangePx = LocalWindowInfo.current.containerSize.height / 3f
    var expansion by remember { mutableFloatStateOf(0f) }
    val settleJob = remember { arrayOfNulls<Job>(1) }
    val expansionConnection = remember(dragRangePx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0f && expansion > 0f && dragRangePx > 0f) {
                    settleJob[0]?.cancel()
                    val current = expansion
                    val next = (current + available.y / dragRangePx).coerceAtLeast(0f)
                    expansion = next
                    return Offset(0f, (next - current) * dragRangePx)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y > 0f && source == NestedScrollSource.UserInput && expansion < 1f && dragRangePx > 0f) {
                    settleJob[0]?.cancel()
                    val current = expansion
                    val next = (current + available.y / dragRangePx).coerceAtMost(1f)
                    expansion = next
                    return Offset(0f, (next - current) * dragRangePx)
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (expansion > 0f && expansion < 1f) {
                    val target = if (expansion >= 0.5f) 1f else 0f
                    settleJob[0]?.cancel()
                    settleJob[0] = scope.launch {
                        animate(expansion, target) { value, _ -> expansion = value }
                    }
                }
                return Velocity.Zero
            }
        }
    }

    val density = LocalDensity.current
    val statusTopPx = WindowInsets.statusBars.getTop(density).toFloat()
    val navBottomPx = WindowInsets.navigationBars.getBottom(density).toFloat()
    val settingsScrollState = rememberScrollState()

    val themeModeFlow = remember { themeModeFlow(context) }
    val themeMode by themeModeFlow.collectAsState(initial = ThemeMode.SYSTEM)

    val speedUnitFlow = remember { speedUnitFlow(context) }
    val speedUnit by speedUnitFlow.collectAsState(initial = SpeedUnit.BYTES_PER_SECOND)

    val dataUnitFlow = remember { dataUnitFlow(context) }
    val dataUnit by dataUnitFlow.collectAsState(initial = DataUnit.BYTES_BINARY)

    fun onThemeModeSelected(mode: ThemeMode, onSaved: () -> Unit) {
        scope.launch {
            setThemeMode(context, mode)
            onSaved()
        }
    }

    fun onSpeedUnitSelected(unit: SpeedUnit, onSaved: () -> Unit) {
        scope.launch {
            setSpeedUnit(context, unit)
            onSaved()
        }
    }

    fun onDataUnitSelected(unit: DataUnit, onSaved: () -> Unit) {
        scope.launch {
            setDataUnit(context, unit)
            onSaved()
        }
    }

    MyComposeApplicationTheme(themeMode = themeMode) {
        val navBarColor = helpSheetNavBarColor ?: MaterialTheme.colorScheme.background
        val sheetScrim by animateColorAsState(
            targetValue = if (helpSheetNavBarColor != null) {
                MaterialTheme.colorScheme.scrim.copy(alpha = 0.32f)
            } else {
                Color.Transparent
            },
            label = "sheetScrim"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(dragRangePx, statusTopPx, navBottomPx, settingsScrollState) {
                    awaitEachGesture {
                        val down = awaitFirstDown(
                            requireUnconsumed = false,
                            pass = PointerEventPass.Initial
                        )
                        if (down.position.y < statusTopPx || down.position.y > size.height - navBottomPx) {
                            return@awaitEachGesture
                        }
                        val slop = viewConfiguration.touchSlop
                        var total = 0f
                        var active = false
                        var moved = false
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Final)
                            val change = event.changes.firstOrNull { it.id == down.id } ?: break
                            if (!change.pressed) break
                            if (change.isConsumed) {
                                total = 0f
                                active = false
                                continue
                            }
                            val dy = change.position.y - change.previousPosition.y
                            total += dy
                            if (!active && abs(total) > slop) active = true
                            if (active && dy != 0f && dragRangePx > 0f) {
                                if (dy > 0f && settingsScrollState.value == 0 && expansion < 1f) {
                                    settleJob[0]?.cancel()
                                    expansion = (expansion + dy / dragRangePx).coerceAtMost(1f)
                                    moved = true
                                    change.consume()
                                } else if (dy < 0f && expansion > 0f) {
                                    settleJob[0]?.cancel()
                                    expansion = (expansion + dy / dragRangePx).coerceAtLeast(0f)
                                    moved = true
                                    change.consume()
                                }
                            }
                        }
                        if (moved && expansion > 0f && expansion < 1f) {
                            val target = if (expansion >= 0.5f) 1f else 0f
                            settleJob[0]?.cancel()
                            settleJob[0] = scope.launch {
                                animate(expansion, target) { value, _ -> expansion = value }
                            }
                        }
                    }
                }
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                    .nestedScroll(expansionConnection),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    SpeedMeterTopAppBar(
                        title = stringResource(id = R.string.app_name),
                        themeMode = themeMode,
                        onQuitClick = { quitApp(activity) },
                        scrollBehavior = topAppBarScrollBehavior,
                        expansion = expansion
                    )
                },
                bottomBar = {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsBottomHeight(WindowInsets.navigationBars)
                            .background(navBarColor)
                    )
                }
            ) { innerPadding ->
                CompositionLocalProvider(LocalOverscrollFactory provides null) {
                    SpeedMeterSettingsScreen(
                        themeMode = themeMode,
                        onThemeModeSelected = ::onThemeModeSelected,
                        speedUnit = speedUnit,
                        onSpeedUnitSelected = ::onSpeedUnitSelected,
                        dataUnit = dataUnit,
                        onDataUnitSelected = ::onDataUnitSelected,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        scrollState = settingsScrollState
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    .background(sheetScrim)
            )
        }
    }
}
