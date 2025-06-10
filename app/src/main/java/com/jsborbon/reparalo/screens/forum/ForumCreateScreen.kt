package com.jsborbon.reparalo.screens.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.ForumTopic
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.Error
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.Success
import com.jsborbon.reparalo.viewmodels.ForumViewModel
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumCreateScreen(
    navController: NavController,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {
    val forumViewModel: ForumViewModel = viewModel()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    val createState by forumViewModel.createState.collectAsState()
    var currentUserId by remember { mutableStateOf("") }
    var currentUserName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        forumViewModel.resetCreateState()
        auth.currentUser?.let { user ->
            currentUserId = user.uid
            val doc = firestore.collection("users").document(user.uid).get().await()
            currentUserName = doc.getString("name") ?: ""
        }
    }

    LaunchedEffect(createState) {
        if (createState is ApiResponse.Success) {
            navController.navigate(Routes.FORUM) {
                popUpTo(Routes.FORUM_CREATE) { inclusive = true }
            }
            forumViewModel.resetCreateState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Nuevo Tema",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.semantics {
                            contentDescription = "Volver"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información del Tema",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryLight
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Categoría") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 8
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank() && category.isNotBlank()) {
                        val topic = ForumTopic(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            category = category,
                            author = Author(uid = currentUserId, name = currentUserName),
                            date = Date(),
                            preview = description.take(100),
                            comments = 0,
                            likes = 0,
                            views = 0,
                        )
                        forumViewModel.createTopic(topic)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Publicar nuevo tema en el foro" },
                enabled = createState !is ApiResponse.Loading &&
                    title.isNotBlank() && description.isNotBlank() && category.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Success,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (createState is ApiResponse.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (createState is ApiResponse.Loading) "Publicando..." else "Publicar tema",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (createState is ApiResponse.Failure) {
                val errorMessage = (createState as? ApiResponse.Failure)?.errorMessage
                    ?: "Ocurrió un error inesperado."
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Error.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = errorMessage,
                        color = Error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
