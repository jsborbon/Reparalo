package com.jsborbon.reparalo.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Avatar(
    imageName: Int,
    borderColor: Color = Color.Blue,
    size: Dp = 100.dp
) {
    Surface(
        modifier = Modifier.size(size),
        shape = CircleShape,
        color = borderColor
    ) {
        Image(
            painter = painterResource(id = imageName),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}
