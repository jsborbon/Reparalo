package com.jsborbon.reparalo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ManagementView(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBottomBar(selectedIndex = 2, navController = navController)
        }
    ) { innerPadding ->
        ManagementViewContent(innerPadding, navController)
    }
}

@Composable
fun ManagementViewContent(innerPadding: PaddingValues, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Management",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manolo Ruíz",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .padding(10.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Key Performance Indicators (KPIs)",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KPIView("Total Sales", "$200K")
            KPIView("Conversion Rate", "15%")
            KPIView("Lead Response", "2h")
            KPIView("Customer LTV", "$2K")
        }

        Section(title = "Campaigns & Sending Groups") {
            ManagementTile("All Campaigns", Icons.Default.Notifications, navController, "allCampaigns")
            ManagementTile("Inactive Campaigns", Icons.Default.ArrowDropDown, navController, "inactiveCampaigns")
            ManagementTile("Sending Groups", Icons.Default.KeyboardArrowUp, navController, "sendingGroups")
        }

        Section(title = "Leads & Customer Insights") {
            ManagementTile("Recent Leads", Icons.Default.Person, navController, "recentLeads")
            ManagementTile("High Priority Leads", Icons.Default.Warning, navController, "highPriorityLeads")
            ManagementTile("Customer Segment Insights", Icons.Default.Search, navController, "customerInsights")
        }

        Section(title = "Automations & Notifications") {
            ManagementTile("All Automations", Icons.Default.Build, navController, "allAutomations")
            ManagementTile("All Notifications", Icons.Default.Notifications, navController, "allNotifications")
        }

        Section(title = "Tasks Overview") {
            ManagementTile("Tasks Dashboard", Icons.Default.ShoppingCart, navController, "tasksDashboard")
            ManagementTile("Upcoming Tasks", Icons.Default.MoreVert, navController, "upcomingTasks")
            ManagementTile("Completed Tasks", Icons.Default.Done, navController, "completedTasks")
        }

        Section(title = "Reviews & Feedback") {
            ManagementTile("Reviews List", Icons.Default.ThumbUp, navController, "reviewsList")
            ManagementTile("Reply to Reviews", Icons.Default.Favorite, navController, "replyReviews")
            ManagementTile("Launch a Survey", Icons.Default.Star, navController, "launchSurvey")
            ManagementTile("Visualize Survey Results", Icons.Default.Email, navController, "surveyResults")
        }
    }
}

@Composable
fun KPIView(title: String, value: String) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ManagementTile(
    title: String,
    icon: ImageVector,
    navController: NavController,
    destination: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .clickable { navController.navigate(destination) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Section(title: String, content: @Composable ColumnScope.() -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        content()
    }
}
