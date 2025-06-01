package com.jsborbon.reparalo.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.history.components.ServiceHistoryCard
import com.jsborbon.reparalo.screens.profile.components.ClientFavoritesCard
import com.jsborbon.reparalo.screens.profile.components.ContactInfoCard
import com.jsborbon.reparalo.screens.profile.components.EditableContactInfoCard
import com.jsborbon.reparalo.screens.profile.components.ProfileHeader
import com.jsborbon.reparalo.screens.technician.components.TechnicianDetailsCard
import com.jsborbon.reparalo.screens.technician.components.TechnicianStatsCard
import com.jsborbon.reparalo.utils.formatDate
import com.jsborbon.reparalo.viewmodels.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    isEditMode: Boolean = false,
    viewModel: UserProfileViewModel = remember { UserProfileViewModel() },
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
    val specialties = user.specialty?.split(",")?.map { it.trim() } ?: emptyList()
    val completedServices = user.completedServices
    val userRating = user.rating
    val satisfaction = user.satisfaction
    val favoriteCount = user.favoriteCount
    val lastServiceTimestamp = user.lastServiceTimestamp

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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ProfileHeader(
                    userName = user.name,
                    isTechnician = userIsTechnician,
                    isVerified = userIsTechnician && satisfaction >= 4.0f,
                    userRating = userRating,
                    completedServices = completedServices,
                )
            }

            item {
                if (editMode.value) {
                    EditableContactInfoCard(
                        name = userName,
                        phone = userPhone,
                        joinDate = userJoinDate,
                        onNameChange = { userName = it },
                        onPhoneChange = { userPhone = it },
                    )
                } else {
                    ContactInfoCard(
                        email = userEmail,
                        phone = userPhone,
                        joinDate = userJoinDate,
                    )
                }
            }

            if (userIsTechnician) {
                item {
                    TechnicianDetailsCard(
                        specialties = specialties,
                        availability = availability.value,
                    )
                }
                item {
                    TechnicianStatsCard(
                        completedServices = completedServices,
                        rating = userRating,
                        satisfaction = satisfaction,
                    )
                }
            } else {
                item {
                    ClientFavoritesCard(
                        favoriteCount = favoriteCount,
                        onViewFavoritesClick = {
                            navController.navigate(Routes.FAVORITES)
                        },
                    )
                }
                item {
                    ServiceHistoryCard(
                        title = "Último servicio",
                        date = lastServiceTimestamp,
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
