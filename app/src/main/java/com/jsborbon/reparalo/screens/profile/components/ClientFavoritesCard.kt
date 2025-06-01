package com.jsborbon.reparalo.screens.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.components.InfoRow

@Composable
fun ClientFavoritesCard(
    favoriteCount: Int,
    onViewFavoritesClick: () -> Unit,
) {
    InfoCard(title = "Mis Favoritos") {
        InfoRow(
            icon = painterResource(id = R.drawable.baseline_favorite),
            text = "$favoriteCount tutoriales guardados",
        )
        Button(
            onClick = onViewFavoritesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Text("Ver mis favoritos")
        }
    }
}
