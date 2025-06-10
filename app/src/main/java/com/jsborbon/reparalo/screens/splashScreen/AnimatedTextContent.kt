package com.jsborbon.reparalo.screens.splashScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsborbon.reparalo.ui.theme.PrimaryDark

@Composable
fun AnimatedTextContent(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(1000, delayMillis = 600, easing = FastOutSlowInEasing)
        ) + slideInVertically(
            initialOffsetY = { -30 },
            animationSpec = tween(1000, delayMillis = 600, easing = FastOutSlowInEasing)
        ),
    ) {
        Text(
            text = "Reparalo",
            color = PrimaryDark,
            fontSize = 44.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.semantics {
                contentDescription = "Reparalo, nombre de la aplicación"
            }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(1000, delayMillis = 900, easing = FastOutSlowInEasing)
        ) + slideInVertically(
            initialOffsetY = { 30 },
            animationSpec = tween(1000, delayMillis = 900, easing = FastOutSlowInEasing)
        ),
    ) {
        Text(
            text = "Repara en Casa",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.semantics {
                contentDescription = "Repara en Casa, eslogan de la aplicación"
            }
        )
    }
}
