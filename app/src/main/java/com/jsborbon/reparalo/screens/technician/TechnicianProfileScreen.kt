package com.jsborbon.reparalo.screens.technician

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.components.NavigationBottomBar
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.screens.profile.ContactInfoCard
import com.jsborbon.reparalo.viewmodels.TechnicianViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianProfileScreen(
    technicianId: String,
    navController: NavController,
    viewModel: TechnicianViewModel = hiltViewModel(),
) {
    val technicianState by viewModel.technician.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(technicianId) {
        viewModel.loadTechnician(technicianId)
    }

    Scaffold(
        bottomBar = {
            NavigationBottomBar(selectedIndex = 2, navController = navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val state = technicianState) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is ApiResponse.Success -> {
                    val technician = state.data
                    val joinDate = technician.registrationDate

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = technician.name,
                                style = MaterialTheme.typography.titleLarge,
                            )

                            ContactInfoCard(
                                email = technician.email,
                                phone = technician.phone,
                                joinDate = joinDate,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (technician.availability.isNotBlank()) {
                                Text(
                                    text = "Disponibilidad: ${technician.availability}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }

                            if (technician.phone.isNotBlank()) {
                                Button(
                                    onClick = {
                                        val phone = technician.phone.trim().replace(" ", "")
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phone"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.padding(top = 16.dp),
                                ) {
                                    Text("Contactar por WhatsApp")
                                }

                                Button(
                                    onClick = {
                                        val phone = technician.phone.trim().replace(" ", "")
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.padding(top = 8.dp),
                                ) {
                                    Text("Llamar")
                                }
                            } else {
                                Text(
                                    text = "Número de teléfono no disponible",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 16.dp),
                                )
                            }
                        }
                    }
                }

                is ApiResponse.Failure -> {
                    Text(text = "Error al cargar el técnico: ${state.errorMessage}")
                }

                else -> Unit
            }
        }
    }
}
