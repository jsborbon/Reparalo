package com.jsborbon.reparalo.models

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val icon: ImageVector? = null,
    val iconPainter: Painter? = null,
    val label: String,
    val route: String,
    val index: Int
)
