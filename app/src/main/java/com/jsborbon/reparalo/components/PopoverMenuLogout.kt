package com.jsborbon.reparalo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.data.ReparaloAuthRepository

@Composable
fun PopoverMenu() {
    val menuExpandedState = remember { mutableStateOf(false) }
    val menuExpanded = menuExpandedState.value

    Box(
        modifier =
        Modifier
            .padding(10.dp)
            .clickable { menuExpandedState.value = !menuExpandedState.value },
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Account",
            tint = Color.Black,
        )
    }

    if (menuExpanded) {
        Box(
            modifier =
            Modifier
                .padding(start = 10.dp)
                .width(150.dp)
                .background(Color.White)
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            menuExpandedState.value = false
                            val repository = ReparaloAuthRepository(
                                auth = TODO(),
                            )
                            repository.signOut()
                        }.padding(8.dp),
                ) {
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}
