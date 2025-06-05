import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R

@Composable
fun ToggleFavoriteButton(
    tutorialId: String,
    favorites: Set<String>,
    onToggle: () -> Unit,
) {
    val isFavorite = tutorialId in favorites

    IconButton(onClick = onToggle) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) R.drawable.baseline_favorite else R.drawable.outline_favorite,
            ),
            contentDescription = if (isFavorite) "Eliminar de favoritos" else "Añadir a favoritos",
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp),
        )
    }
}
