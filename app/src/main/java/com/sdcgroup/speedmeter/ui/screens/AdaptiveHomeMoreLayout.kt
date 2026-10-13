package com.sdcgroup.speedmeter.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarScrollBehavior
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sdcgroup.speedmeter.ui.utils.WindowWidthSizeClass
import com.sdcgroup.speedmeter.ui.utils.rememberWindowWidthSizeClass
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SpeedMeterHomeMoreContent(
    bottomToolbarScrollBehavior: FloatingToolbarScrollBehavior,
    modifier: Modifier = Modifier
) {
    val widthSizeClass = rememberWindowWidthSizeClass()
    val isSinglePane = widthSizeClass == WindowWidthSizeClass.ExtraCompact ||
        widthSizeClass == WindowWidthSizeClass.Compact

    val baseDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val directive = remember(baseDirective, isSinglePane) {
        baseDirective.copy(maxHorizontalPartitions = if (isSinglePane) 1 else 2)
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>(scaffoldDirective = directive)
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        ListDetailPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top
                        ) {
                            HomeScreen()
                        }
                    }
                }
            },
            detailPane = {
                AnimatedPane {
                    MoreScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )

        if (isSinglePane) {
            SpeedMeterBottomToolbar(
                onHomeClick = {
                    coroutineScope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.List)
                    }
                },
                onMoreClick = {
                    coroutineScope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                scrollBehavior = bottomToolbarScrollBehavior
            )
        }
    }
}
