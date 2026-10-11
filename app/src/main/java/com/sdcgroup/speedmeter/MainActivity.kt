package com.sdcgroup.speedmeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.sdcgroup.speedmeter.ui.screens.SpeedMeterTopAppBar
import com.sdcgroup.speedmeter.ui.theme.MyComposeApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedMeterApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedMeterApp() {
    val systemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by rememberSaveable { mutableStateOf(systemDarkTheme) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    MyComposeApplicationTheme(darkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                SpeedMeterTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = it },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {}
        }
    }
}
