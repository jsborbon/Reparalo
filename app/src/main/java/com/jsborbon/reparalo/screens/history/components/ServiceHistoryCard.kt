package com.jsborbon.reparalo.screens.history.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.ui.theme.Success
import com.jsborbon.reparalo.utils.formatDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ServiceStatus {
    COMPLETED, PENDING, SCHEDULED, HISTORY
}

@Composable
fun serviceStatusData(status: ServiceStatus): Triple<Painter, Color, String> {
    return when (status) {
        ServiceStatus.COMPLETED -> Triple(
            painterResource(R.drawable.outline_check_circle),
            Success,
            "Completado"
        )
        ServiceStatus.PENDING -> Triple(
            painterResource(R.drawable.outline_pending_actions),
            RepairYellow,
            "Pendiente"
        )
        ServiceStatus.SCHEDULED -> Triple(
            painterResource(R.drawable.outline_schedule),
            PrimaryLight,
            "Programado"
        )
        ServiceStatus.HISTORY -> Triple(
            painterResource(R.drawable.baseline_history),
            PrimaryLight,
            "Historial"
        )
    }
}

@Composable
fun ServiceHistoryCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    date: Long,
    status: ServiceStatus = ServiceStatus.HISTORY,
    showButton: Boolean = false,
    buttonText: String = "Ver historial completo",
    onButtonClick: (() -> Unit)? = null,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null,
    showStatusIcon: Boolean = true,
    maxLines: Int = 3
) {
    val (painter, color, label) = serviceStatusData(status)
    val backgroundColor = color.copy(alpha = 0.1f)

    var isExpanded by remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(
        targetValue = if (isClickable && onClick != null) 1.02f else 1.0f,
        animationSpec = tween(200),
        label = "scaleAnim"
    )
    val semanticDescription = "Servicio: $title, fecha: ${formatDate(date)}, estado: $label"

    val clickableModifier = if (isClickable && onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else Modifier

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .then(clickableModifier)
            .semantics { contentDescription = semanticDescription },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showStatusIcon) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(color.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(painter = painter, contentDescription = null, tint = color)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = getRelativeTimeString(date),
                        style = MaterialTheme.typography.labelMedium,
                        color = color
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.Medium
                )
            }

            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (showButton && onButtonClick != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = color)
                ) {
                    Text(text = buttonText)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.outline_chevron_right),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

private fun getRelativeTimeString(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "Hace un momento"
        diff < 3_600_000 -> "Hace ${diff / 60_000} min"
        diff < 86_400_000 -> "Hace ${diff / 3_600_000} h"
        diff < 2_592_000_000 -> "Hace ${diff / 86_400_000} días"
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}
