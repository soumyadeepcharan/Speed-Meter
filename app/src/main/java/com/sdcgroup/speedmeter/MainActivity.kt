package com.sdcgroup.speedmeter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.sdcgroup.speedmeter.data.DataUnit
import com.sdcgroup.speedmeter.data.SpeedUnit
import com.sdcgroup.speedmeter.data.ThemeMode
import com.sdcgroup.speedmeter.data.dataUnitFlow
import com.sdcgroup.speedmeter.data.indicatorEnabledFlow
import com.sdcgroup.speedmeter.data.setDataUnit
import com.sdcgroup.speedmeter.data.setSpeedUnit
import com.sdcgroup.speedmeter.data.setThemeMode
import com.sdcgroup.speedmeter.data.speedUnitFlow
import com.sdcgroup.speedmeter.data.themeModeFlow
import com.sdcgroup.speedmeter.service.SpeedMonitorService
import com.sdcgroup.speedmeter.ui.MyComposeApplicationTheme
import com.sdcgroup.speedmeter.ui.SpeedMeterHomeMoreContent
import com.sdcgroup.speedmeter.ui.SpeedMeterTopAppBar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
        enableEdgeToEdge()

        lifecycleScope.launch {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedMeterApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            topBar = {
                SpeedMeterTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    themeMode = themeMode,
                    scrollBehavior = topAppBarScrollBehavior
                )
            }
        ) { innerPadding ->
            SpeedMeterHomeMoreContent(
                themeMode = themeMode,
                onThemeModeSelected = ::onThemeModeSelected,
                speedUnit = speedUnit,
                onSpeedUnitSelected = ::onSpeedUnitSelected,
                dataUnit = dataUnit,
                onDataUnitSelected = ::onDataUnitSelected,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
