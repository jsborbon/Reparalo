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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.components.InfoRow
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.history.ServiceHistoryCard
import com.jsborbon.reparalo.screens.technician.TechnicianDetailsCard
import com.jsborbon.reparalo.screens.technician.TechnicianStatsCard
import com.jsborbon.reparalo.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    isEditMode: Boolean = false
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val editMode = remember { mutableStateOf(isEditMode) }

    var userIsTechnician by remember { mutableStateOf(true) }
    var userName by remember { mutableStateOf(currentUser?.displayName ?: "Usuario") }
    var userEmail by remember { mutableStateOf(currentUser?.email ?: "correo@ejemplo.com") }
    var userPhone by remember { mutableStateOf("123456789") }
    val userPhoto = remember { mutableStateOf(currentUser?.photoUrl?.toString() ?: "") }
    var userRating by remember { mutableFloatStateOf(4.8f) }
    val userJoinDate = remember {
        formatDate(currentUser?.metadata?.creationTimestamp ?: System.currentTimeMillis())
    }

    val specialties = remember { mutableStateOf(listOf("Electricidad", "Plomería", "Carpintería")) }
    val completedServices = remember { mutableIntStateOf(24) }
    val isVerified = remember { mutableStateOf(true) }
    val availability = remember { mutableStateOf("Lunes a Viernes, 9:00 - 18:00") }

    val favoriteCount = remember { mutableIntStateOf(12) }
    val lastServiceTimestamp = remember { mutableLongStateOf(System.currentTimeMillis() - 86400000L * 3) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    IconButton(onClick = { /*TODO Future settings */ }) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProfileHeader(
                    userName = userName,
                    userPhoto = userPhoto.value,
                    isTechnician = userIsTechnician,
                    isVerified = isVerified.value,
                    userRating = userRating,
                    completedServices = completedServices.intValue
                )
            }

            item {
                InfoCard(title = "Información de contacto") {
                    if (editMode.value) {
                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = userPhone,
                            onValueChange = { userPhone = it },
                            label = { Text("Teléfono") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Miembro desde: $userJoinDate",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        InfoRow(
                            icon = painterResource(id = com.jsborbon.reparalo.R.drawable.baseline_email),
                            text = userEmail
                        )
                        InfoRow(
                            icon = painterResource(id = com.jsborbon.reparalo.R.drawable.baseline_phone),
                            text = userPhone
                        )
                        InfoRow(
                            icon = painterResource(id = com.jsborbon.reparalo.R.drawable.baseline_date_range),
                            text = "Miembro desde: $userJoinDate"
                        )
                    }
                }
            }

            if (userIsTechnician) {
                item {
                    TechnicianDetailsCard(
                        specialties = specialties.value,
                        availability = availability.value
                    )
                }

                item {
                    TechnicianStatsCard(
                        completedServices = completedServices.intValue,
                        rating = userRating
                    )
                }
            } else {
                item {
                    ClientFavoritesCard(
                        favoriteCount = favoriteCount.intValue,
                        onViewFavoritesClick = {
                            navController.navigate(Routes.FAVORITES)
                        }
                    )
                }

                item {
                    ServiceHistoryCard(
                        title = "Último servicio",
                        date = lastServiceTimestamp.longValue,
                        showButton = true,
                        onButtonClick = {
                            navController.navigate(Routes.SERVICE_HISTORY)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                if (editMode.value) {
                    Button(
                        onClick = {
                            //TODO Save changes logic here
                            navController.navigate(Routes.USER_PROFILE)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar cambios")
                    }
                } else {
                    Button(
                        onClick = {
                            navController.navigate("${Routes.USER_PROFILE}?edit=true")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
