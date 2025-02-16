package com.jsborbon.relato.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Marquee(text: String) {
    val textWidth = remember { getTextWidth(text) }
    val containerWidth = remember { 1000f }
    var offsetX by remember { mutableFloatStateOf(containerWidth) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16)
            offsetX -= 2f
            if (offsetX < -textWidth) {
                offsetX = containerWidth
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicText(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = offsetX.dp)
        )
    }
}

fun getTextWidth(text: String): Float {
    val paint = android.graphics.Paint()
    paint.textSize = 20f
    return paint.measureText(text)
}

