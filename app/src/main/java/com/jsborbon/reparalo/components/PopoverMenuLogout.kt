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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@Composable
fun PopoverMenu(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val isExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .clickable { isExpanded.value = !isExpanded.value },
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "User menu",
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }

    if (isExpanded.value) {
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .width(160.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                MenuItem(
                    text = "Cerrar sesión",
                    onClick = {
                        isExpanded.value = false
                        viewModel.logout()
                        navController.navigate(Routes.AUTHENTICATION) {
                            popUpTo(Routes.DASHBOARD) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
