package com.jsborbon.reparalo.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.navigation.components.NavigationBottomBar
import com.jsborbon.reparalo.screens.profile.components.UserCard
import com.jsborbon.reparalo.viewmodels.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
) {
    val viewModel: UserProfileViewModel = viewModel()

    val state by viewModel.user.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state) {
        if (state is ApiResponse.Success) {
            val user = (state as ApiResponse.Success).data
            name = user.name
            phone = user.phone
            availability = user.availability
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBottomBar(
                selectedIndex = 5,
                navController = navController,
                onTabChange = {},
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val result = state) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 48.dp))
                }

                is ApiResponse.Failure -> {
                    Text(
                        text = "Error al cargar el perfil: ${result.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                is ApiResponse.Success -> {
                    val user = result.data

                    UserCard(user = user, padding = PaddingValues(bottom = 24.dp))

                    Text(
                        text = "Perfil de Usuario",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = availability,
                        onValueChange = { availability = it },
                        label = { Text("Disponibilidad") },
                        placeholder = { Text("Ej: Lunes a Viernes 8:00 a 18:00") },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = user.email,
                        onValueChange = {},
                        label = { Text("Correo electrónico") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = user.userType.name,
                        onValueChange = {},
                        label = { Text("Tipo de Usuario") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.updateUserData(name, phone, availability)
                            message = "Cambios guardados correctamente"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    ) {
                        Text("Guardar Cambios")
                    }

                    TextButton(
                        onClick = { navController.navigate(Routes.SETTINGS_PASSWORD) },
                    ) {
                        Text("Cambiar contraseña")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    message?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            navController.navigate(Routes.AUTHENTICATION) {
                                popUpTo(Routes.DASHBOARD) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Cerrar Sesión")
                    }
                }

                else -> {
                    Text(
                        text = "Cargando perfil...",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
