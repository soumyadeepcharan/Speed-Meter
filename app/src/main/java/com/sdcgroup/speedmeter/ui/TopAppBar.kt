package com.sdcgroup.speedmeter.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.sdcgroup.speedmeter.data.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedMeterTopAppBar(
    title: String,
    themeMode: ThemeMode,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier
) {
    key(themeMode) {
        TopAppBar(
            modifier = modifier,
            title = { Text(title) },
            scrollBehavior = scrollBehavior
        )
    }
}
