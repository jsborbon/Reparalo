package com.jsborbon.reparalo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.models.Communication
import java.util.UUID


@Composable
fun CommunicationsView(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add Communication")
            }
        },
        bottomBar = {
            NavigationBottomBar(selectedIndex = 3, navController = navController)
        }
    ) { innerPadding ->
        CommunicationsViewContent(innerPadding, navController)
    }
}

@Composable
fun CommunicationsViewContent(innerPadding: PaddingValues, navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var showNewCommunicationDialog by remember { mutableStateOf(false) }

    val availableCategories = listOf("All", "Announcements", "Ticketing", "Reminders", "Products")

    var communications by remember {
        mutableStateOf(
            listOf(
                Communication(
                    UUID.randomUUID(),
                    "Q4 Keynote Reminder",
                    "Details about the upcoming keynote.",
                    "Investors Newsletter",
                    "Reminders",
                    false
                ),
                Communication(
                    UUID.randomUUID(),
                    "Product Tips",
                    "Learn how to use our new features.",
                    "Tips Newsletter",
                    "Products",
                    false
                ),
                Communication(
                    UUID.randomUUID(),
                    "Weekly Update",
                    "Updates from this week.",
                    "Weekly Digest",
                    "Announcements",
                    false
                ),
                Communication(
                    UUID.randomUUID(),
                    "Project Deadline",
                    "Reminder about the upcoming deadline.",
                    "Reminders & Deadlines",
                    "Ticketing",
                    false
                )
            )
        )
    }

    val filteredCommunications = communications.filter { communication ->
        (selectedCategory == null || selectedCategory == "All" || communication.campaign == selectedCategory) &&
                (searchText.isEmpty() || communication.subject.contains(searchText, ignoreCase = true) ||
                        communication.sendingGroup.contains(searchText, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        HeaderSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search communications...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categories Section
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            availableCategories.forEach { category ->
                Text(
                    text = category,
                    color = if (category == selectedCategory) MaterialTheme.colorScheme.primary else Color.Black,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { selectedCategory = category }
                        .padding(8.dp),
                    fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Communications List
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)
        ) {
            filteredCommunications.forEach { communication ->
                CommunicationRow(communication)
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            }
        }

        // Floating Action Button
        if (showNewCommunicationDialog) {
            // Show dialog (implement dialog logic here if necessary)
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome to",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Communications",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Manolo Ruíz",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Icon(
            imageVector = Icons.Default.MailOutline,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .padding(10.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CommunicationRow(communication: Communication) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(communication.subject, fontWeight = FontWeight.Bold)
            Text(communication.sendingGroup, color = Color.Gray)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
    }
}
