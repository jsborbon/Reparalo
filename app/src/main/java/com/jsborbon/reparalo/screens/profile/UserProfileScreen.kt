package com.jsborbon.reparalo.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.history.ServiceHistoryCard
import com.jsborbon.reparalo.screens.technician.TechnicianDetailsCard
import com.jsborbon.reparalo.screens.technician.TechnicianStatsCard
import com.jsborbon.reparalo.utils.formatDate
import com.jsborbon.reparalo.viewmodels.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    isEditMode: Boolean = false,
    viewModel: UserProfileViewModel = remember { UserProfileViewModel() }
) {
    val editMode = remember { mutableStateOf(isEditMode) }
    val userState by viewModel.user.collectAsState()

    val user = when (val state = userState) {
        is ApiResponse.Success -> state.data
        is ApiResponse.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }
        is ApiResponse.Failure -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage)
            }
            return
        }
    }

    var userName by remember { mutableStateOf(user.name) }
    var userPhone by remember { mutableStateOf(user.phone) }
    val userEmail = user.email
    val userJoinDate = if (user.registrationDate.isNotBlank()) formatDate(user.registrationDate) else "Desconocido"
    val userIsTechnician = user.userType.name == "TECHNICIAN"
    val availability = remember { mutableStateOf(user.availability) }

    val specialties = remember { mutableStateOf(listOf("Electricidad", "Plomería", "Carpintería")) }
    val completedServices = remember { mutableIntStateOf(24) }
    val userRating = remember { mutableFloatStateOf(user.rating) }
    val favoriteCount = remember { mutableIntStateOf(12) }
    val lastServiceTimestamp = remember { mutableLongStateOf(System.currentTimeMillis() - 86400000L * 3) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Configuración")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                if (editMode.value) {
                    InfoCard(title = "Información de contacto") {
                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                        )
                        OutlinedTextField(
                            value = userPhone,
                            onValueChange = { userPhone = it },
                            label = { Text("Teléfono") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = "Miembro desde: $userJoinDate",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                } else {
                    ContactInfoCard(
                        email = userEmail,
                        phone = userPhone,
                        joinDate = userJoinDate,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (userIsTechnician) {
                item {
                    TechnicianDetailsCard(
                        specialties = specialties.value,
                        availability = availability.value,
                    )
                }
                item {
                    TechnicianStatsCard(
                        completedServices = completedServices.intValue,
                        rating = userRating.floatValue,
                    )
                }
            } else {
                item {
                    ClientFavoritesCard(
                        favoriteCount = favoriteCount.intValue,
                        onViewFavoritesClick = {
                            navController.navigate(Routes.FAVORITES)
                        },
                    )
                }
                item {
                    ServiceHistoryCard(
                        title = "Último servicio",
                        date = lastServiceTimestamp.longValue,
                        showButton = true,
                        onButtonClick = {
                            navController.navigate(Routes.SERVICE_HISTORY)
                        },
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                if (editMode.value) {
                    Button(
                        onClick = {
                            viewModel.updateUserData(
                                name = userName,
                                phone = userPhone,
                                availability = availability.value,
                            )
                            navController.navigate(Routes.userProfileRoute(edit = false))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Guardar cambios")
                    }
                } else {
                    Button(
                        onClick = {
                            navController.navigate(Routes.userProfileRoute(edit = true))
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar perfil",
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text("Editar perfil")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Routes.AUTHENTICATION) {
                            popUpTo(Routes.DASHBOARD) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
