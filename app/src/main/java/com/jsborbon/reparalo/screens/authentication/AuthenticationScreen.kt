package com.jsborbon.reparalo.screens.authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@Composable
fun AuthenticationScreen(
    navController: NavController,
    viewModel: AuthViewModel = remember { AuthViewModel() }
) {
    var isLogin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Reparalo",
            fontSize = 36.sp,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Tu asistente de reparaciones",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(32.dp))

        TabRow(selectedTabIndex = if (isLogin) 0 else 1) {
            Tab(
                selected = isLogin,
                onClick = { isLogin = true },
                text = { Text("Iniciar Sesión") },
            )
            Tab(
                selected = !isLogin,
                onClick = { isLogin = false },
                text = { Text("Registrarse") },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLogin) {
            LoginForm(navController = navController, viewModel = viewModel)
        } else {
            SignUpForm(navController = navController, viewModel = viewModel)
        }
    }
}
