package com.jsborbon.reparalo.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.components.states.*
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.AvailabilitySlot
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.components.LoadingIndicator
import com.jsborbon.reparalo.ui.theme.*
import com.jsborbon.reparalo.viewmodels.UserProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(navController: NavHostController) {
    val viewModel: UserProfileViewModel = viewModel()
    val userState by viewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf<List<AvailabilitySlot>>(emptyList()) }
    var hasChanged by remember { mutableStateOf(false) }
    var snackbarText by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userState) {
        if (userState is ApiResponse.Success) {
            val user = (userState as ApiResponse.Success).data
            name = user.name
            phone = user.phone
            availability = user.availability
        }
    }

    LaunchedEffect(snackbarText) {
        snackbarText?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
            snackbarText = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = "Pantalla del perfil del usuario" }
    ) { padding ->
        when (val state = userState) {
            is ApiResponse.Idle -> LoadingIndicator()

            is ApiResponse.Loading -> LoadingState(message = "Cargando perfil...")

            is ApiResponse.Failure -> ErrorState(
                message = state.errorMessage,
                onRetry = { viewModel.loadUserData() }
            )

            is ApiResponse.Success -> {
                val user = state.data
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = padding.calculateTopPadding() + 16.dp,
                        bottom = padding.calculateBottomPadding() + 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    item {
                        Text(
                            text = "👤 Mi Perfil",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryLight
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                hasChanged = true
                            },
                            label = { Text("Nombre completo") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RepairYellow)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = {
                                phone = it
                                hasChanged = true
                            },
                            label = { Text("Teléfono") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RepairYellow)
                        )
                    }

                    item {
                        Text(
                            text = "Disponibilidad: ${availability.size} franjas definidas",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = user.email,
                            onValueChange = {},
                            label = { Text("Correo electrónico") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryLight,
                                disabledBorderColor = PrimaryLight.copy(alpha = 0.4f)
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = user.userType.name,
                            onValueChange = {},
                            label = { Text("Tipo de usuario") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryLight,
                                disabledBorderColor = PrimaryLight.copy(alpha = 0.4f)
                            )
                        )
                    }

                    item {
                        if (hasChanged) {
                            Button(
                                onClick = {
                                    viewModel.updateUserData(name, phone, availability.toString())
                                    hasChanged = false
                                    snackbarText = "✅ Perfil actualizado correctamente"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RepairYellow),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Done, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Guardar Cambios")
                            }
                        } else {
                            Text(
                                text = "✅ Todos los cambios están guardados",
                                color = Success,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    item {
                        OutlinedButton(
                            onClick = { navController.navigate(Routes.CHANGE_PASSWORD) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cambiar contraseña")
                        }
                    }

                    item {
                        OutlinedButton(
                            onClick = { navController.navigate(Routes.FAVORITES) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ver favoritos")
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                navController.navigate(Routes.AUTHENTICATION) {
                                    popUpTo(Routes.DASHBOARD) { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Error),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cerrar sesión")
                        }
                    }

                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}
