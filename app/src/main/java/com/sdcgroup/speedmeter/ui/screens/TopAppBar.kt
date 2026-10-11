package com.sdcgroup.speedmeter.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sdcgroup.speedmeter.ui.theme.MyComposeApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedMeterTopAppBar(
    title: String,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title) },
        scrollBehavior = scrollBehavior
    )
}
