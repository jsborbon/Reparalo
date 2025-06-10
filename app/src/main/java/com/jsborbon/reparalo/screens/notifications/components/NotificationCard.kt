package com.jsborbon.reparalo.screens.notifications.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.NotificationItem
import com.jsborbon.reparalo.ui.theme.Error
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.ui.theme.Success
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Notification types with visual styling
enum class NotificationType(
    val icon: ImageVector,
    val color: Color,
    val backgroundColor: Color
) {
    INFO(Icons.Default.Info, PrimaryLight, PrimaryLight.copy(alpha = 0.1f)),
    SUCCESS(Icons.Default.CheckCircle, Success, Success.copy(alpha = 0.1f)),
    WARNING(Icons.Default.Warning, RepairYellow, RepairYellow.copy(alpha = 0.1f)),
    ERROR(Icons.Default.Clear, Error, Error.copy(alpha = 0.1f)),
}

@Composable
fun NotificationCard(
    modifier: Modifier = Modifier,
        notification: NotificationItem,
    onMarkAsRead: ((String) -> Unit)? = null,
    onDismiss: ((String) -> Unit)? = null,
    onClick: ((NotificationItem) -> Unit)? = null,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isDismissed by remember { mutableStateOf(false) }

    // Determine notification type based on content or priority
    val notificationType = remember(notification) {
        when {
            notification.title.contains("error", ignoreCase = true) ||
                notification.message.contains("error", ignoreCase = true) -> NotificationType.ERROR

            notification.title.contains("éxito", ignoreCase = true) ||
                notification.title.contains("completado", ignoreCase = true) ||
                notification.message.contains("éxito", ignoreCase = true) -> NotificationType.SUCCESS

            notification.title.contains("advertencia", ignoreCase = true) ||
                notification.title.contains("atención", ignoreCase = true) ||
                notification.message.contains("advertencia", ignoreCase = true) -> NotificationType.WARNING

            else -> NotificationType.INFO
        }
    }

    // Format timestamp if available
    val formattedTime = remember(notification) {
        try {
            // Assuming notification has a timestamp field
            val timestamp = notification.timestamp
            val dateFormat = SimpleDateFormat("HH:mm • dd/MM", Locale.getDefault())
            dateFormat.format(Date(timestamp))
        } catch (_: Exception) {
            "Ahora"
        }
    }

    // Enhanced card with animation
    AnimatedVisibility(
        visible = !isDismissed,
        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    if (onClick != null) {
                        onClick(notification)
                        // Mark as read when clicked
                        if (!notification.isRead && onMarkAsRead != null) {
                            onMarkAsRead(notification.id)
                        }
                    } else {
                        isExpanded = !isExpanded
                    }
                }
                .semantics {
                    contentDescription = "Notificación: ${notification.title}. ${if (notification.isRead) "Leída" else "No leída"}"
                },
            colors = CardDefaults.cardColors(
                containerColor = if (notification.isRead) {
                    MaterialTheme.colorScheme.surface
                } else {
                    notificationType.backgroundColor
                }
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (notification.isRead) 2.dp else 4.dp
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Enhanced header with icon and actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Enhanced icon and content section
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Notification type icon with background
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = notificationType.color.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = notificationType.icon,
                                contentDescription = null,
                                tint = notificationType.color,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Enhanced content section
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            // Enhanced title with read status
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = notification.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.SemiBold,
                                    color = if (notification.isRead) {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                // Unread indicator dot
                                if (!notification.isRead) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                color = notificationType.color,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Enhanced message with expandable content
                            Text(
                                text = notification.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Enhanced timestamp
                            Text(
                                text = formattedTime,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Action buttons
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        // Dismiss button
                        if (onDismiss != null) {
                            IconButton(
                                onClick = {
                                    isDismissed = true
                                    onDismiss(notification.id)
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .semantics { contentDescription = "Descartar notificación" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Enhanced expandable content
                if (isExpanded && notification.message.length > 100) {
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))

                            // Additional content section
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = "Detalles completos",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = notificationType.color
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = notification.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Enhanced action buttons row
                if (!notification.isRead && onMarkAsRead != null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        androidx.compose.material3.TextButton(
                            onClick = { onMarkAsRead(notification.id) },
                            modifier = Modifier.semantics {
                                contentDescription = "Marcar como leída"
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Success,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Marcar como leída",
                                color = Success,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
