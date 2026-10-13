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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection.Companion.Bottom
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.sdcgroup.speedmeter.data.indicatorEnabledFlow
import com.sdcgroup.speedmeter.service.SpeedMonitorService
import com.sdcgroup.speedmeter.ui.screens.SpeedMeterHomeMoreContent
import com.sdcgroup.speedmeter.ui.screens.SpeedMeterTopAppBar
import com.sdcgroup.speedmeter.ui.theme.MyComposeApplicationTheme
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
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

        setContent {
            SpeedMeterApp()
        }
    }

    private fun startServiceIfEnabled() {
        lifecycleScope.launch {
            indicatorEnabledFlow(this@MainActivity).collect { isEnabled ->
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
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SpeedMeterApp() {
    val context = LocalContext.current
    val systemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by rememberSaveable { mutableStateOf(systemDarkTheme) }
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val bottomToolbarScrollBehavior = FloatingToolbarDefaults.exitAlwaysScrollBehavior(exitDirection = Bottom)

    val enabledFlow = remember { indicatorEnabledFlow(context) }
    val indicatorEnabled by enabledFlow.collectAsState(initial = false)

    MyComposeApplicationTheme(darkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomToolbarScrollBehavior),
            topBar = {
                SpeedMeterTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = it },
                    scrollBehavior = topAppBarScrollBehavior
                )
            }
        ) { innerPadding ->
            SpeedMeterHomeMoreContent(
                isDarkTheme = isDarkTheme,
                onThemeToggle = { isDarkTheme = it },
                bottomToolbarScrollBehavior = bottomToolbarScrollBehavior,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
