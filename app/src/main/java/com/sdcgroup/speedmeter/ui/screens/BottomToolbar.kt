package com.sdcgroup.speedmeter.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarScrollBehavior
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sdcgroup.speedmeter.R
import com.sdcgroup.speedmeter.ui.utils.WindowHeightSizeClass
import com.sdcgroup.speedmeter.ui.utils.WindowWidthSizeClass
import com.sdcgroup.speedmeter.ui.utils.rememberWindowSizeClass

@Composable
private fun RowScope.ToolbarItem(
    icon: ImageVector,
    label: String,
    showLabel: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                role = Role.Tab,
                onClickLabel = label,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = LocalContentColor.current
        )
        if (showLabel) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = LocalContentColor.current
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SpeedMeterBottomToolbar(
    onHomeClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    scrollBehavior: FloatingToolbarScrollBehavior? = null
) {
    val (widthSizeClass, heightSizeClass) = rememberWindowSizeClass()

    val showLabels = !(
        widthSizeClass == WindowWidthSizeClass.ExtraCompact ||
        (widthSizeClass == WindowWidthSizeClass.ExtraCompact && heightSizeClass == WindowHeightSizeClass.Compact)
    )

    val homeLabel = stringResource(id = R.string.home_nav_label)
    val moreLabel = stringResource(id = R.string.more_nav_label)

    HorizontalFloatingToolbar(
        modifier = modifier
            .offset(y = -FloatingToolbarDefaults.ScreenOffset)
            .zIndex(1f),
        expanded = expanded,
        scrollBehavior = scrollBehavior,
        content = {
            ToolbarItem(
                icon = Icons.Filled.Home,
                label = homeLabel,
                showLabel = showLabels,
                selected = false,
                onClick = onHomeClick
            )
            ToolbarItem(
                icon = Icons.Filled.MoreVert,
                label = moreLabel,
                showLabel = showLabels,
                selected = false,
                onClick = onMoreClick
            )
        }
    )
}
