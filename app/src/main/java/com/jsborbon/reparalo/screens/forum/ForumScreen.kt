package com.jsborbon.reparalo.screens.forum

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Category
import com.jsborbon.reparalo.models.ForumTopic
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.forum.components.ForumTopicItem
import com.jsborbon.reparalo.viewmodels.CategoryViewModel
import com.jsborbon.reparalo.viewmodels.ForumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    navController: NavController,
    viewModel: ForumViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
) {
    val categoriesState by categoryViewModel.categories.collectAsState()
    val topicsState by viewModel.topics.collectAsState()

    val allCategories = when (categoriesState) {
        is ApiResponse.Success -> {
            listOf(Category(id = "all", name = "Todos")) +
                (categoriesState as ApiResponse.Success<List<Category>>).data
        }
        else -> listOf(Category(id = "all", name = "Todos"))
    }

    var selectedCategory by remember { mutableStateOf(allCategories.first()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foro") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Routes.FORUM_SEARCH)
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar en el foro")
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
        ) {
            ScrollableTabRow(
                selectedTabIndex = allCategories.indexOfFirst { it.name == selectedCategory.name },
                edgePadding = 0.dp,
                divider = {},
            ) {
                allCategories.forEach { category ->
                    Tab(
                        selected = selectedCategory.name == category.name,
                        onClick = { selectedCategory = category },
                    ) {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                            color = if (selectedCategory.name == category.name) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Routes.FORUM_CREATE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Crear nuevo tema")
            }

            when (topicsState) {
                is ApiResponse.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ApiResponse.Failure -> {
                    val message = (topicsState as ApiResponse.Failure).errorMessage
                    Text(
                        text = "Error: $message",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }

                is ApiResponse.Success -> {
                    val topics = (topicsState as ApiResponse.Success<List<ForumTopic>>).data
                        .filter {
                            selectedCategory.name == "Todos" || it.category == selectedCategory.name
                        }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp),
                    ) {
                        items(topics, key = { it.id }) { topic ->
                            ForumTopicItem(topic = topic)
                        }
                    }
                }
            }
        }
    }
}
