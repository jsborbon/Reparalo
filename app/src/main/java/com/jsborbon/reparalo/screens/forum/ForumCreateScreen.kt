package com.jsborbon.reparalo.screens.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.ForumTopic
import com.jsborbon.reparalo.navigation.Routes
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
        auth.currentUser?.let { user ->
            currentUserId = user.uid
            val doc = firestore.collection("users").document(user.uid).get().await()
            currentUserName = doc.getString("name") ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Tema") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
            )

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
                modifier = Modifier.fillMaxWidth(),
                enabled = createState !is ApiResponse.Loading,
            ) {
                Text("Publicar tema")
            }

            when (createState) {
                is ApiResponse.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(Routes.FORUM) {
                            popUpTo(Routes.FORUM_CREATE) { inclusive = true }
                        }
                    }
                }

                is ApiResponse.Failure -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = (createState as ApiResponse.Failure).errorMessage,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                else -> Unit
            }
        }
    }
}
