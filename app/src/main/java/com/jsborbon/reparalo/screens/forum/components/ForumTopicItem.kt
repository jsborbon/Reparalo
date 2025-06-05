package com.jsborbon.reparalo.screens.forum.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.models.ForumTopic
import com.jsborbon.reparalo.utils.formatDate

@Composable
fun ForumTopicItem(
    topic: ForumTopic,
    onClick: (() -> Unit)? = null,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = topic.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Publicado por ${topic.author.name} el ${formatDate(topic.date.time)}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ForumStat(painter = painterResource(R.drawable.baseline_visibility), value = topic.views.toString())
                ForumStat(icon = Icons.Default.ThumbUp, value = topic.likes.toString())
                ForumStat(painter = painterResource(R.drawable.baseline_comment), value = topic.comments.toString())
            }
        }
    }
}

@Composable
private fun ForumStat(icon: ImageVector, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(
            text = value,
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun ForumStat(painter: Painter, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painter, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(
            text = value,
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
