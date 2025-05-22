package com.jsborbon.reparalo.screens.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.history.ServiceHistoryCard
import com.jsborbon.reparalo.screens.technician.TechnicianDetailsCard
import com.jsborbon.reparalo.screens.technician.TechnicianStatsCard
import com.jsborbon.reparalo.utils.formatDate
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    isEditMode: Boolean = false,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val editMode = remember { mutableStateOf(isEditMode) }
    val userState by authViewModel.user.collectAsState()
    val user = userState ?: return

    var userName by remember { mutableStateOf(user.name) }
    var userPhone by remember { mutableStateOf(user.phone) }
    val userEmail = user.email
    val userJoinDate = if (user.registrationDate.isNotBlank()) {
        formatDate(user.registrationDate)
    } else {
        "Desconocido"
    }
    val userIsTechnician = user.userType.name == "TECNICO"
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
                },
            )
        },
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
                            authViewModel.updateUser(
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
