package com.jsborbon.relato

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
            Image(
                painter = painterResource(id = R.drawable.ic_mail_and_text_magnifyingglass),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .padding(10.dp),
                contentScale = ContentScale.Fit
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
            KPIView(title = "Total Sales", value = "$200K")
            KPIView(title = "Conversion Rate", value = "15%")
            KPIView(title = "Lead Response", value = "2h")
            KPIView(title = "Customer LTV", value = "$2K")
        }

        Section(title = "Campaigns & Sending Groups") {
            ManagementTile(
                title = "All Campaigns",
                icon = R.drawable.ic_campaign,
                navController = navController,
                destination = "allCampaigns"
            )
            ManagementTile(
                title = "Inactive Campaigns",
                icon = R.drawable.ic_inactive_campaign,
                navController = navController,
                destination = "inactiveCampaigns"
            )
            ManagementTile(
                title = "Sending Groups",
                icon = R.drawable.ic_sending_groups,
                navController = navController,
                destination = "sendingGroups"
            )
        }

        Section(title = "Leads & Customer Insights") {
            ManagementTile(
                title = "Recent Leads",
                icon = R.drawable.ic_recent_leads,
                navController = navController,
                destination = "recentLeads"
            )
            ManagementTile(
                title = "High Priority Leads",
                icon = R.drawable.ic_high_priority,
                navController = navController,
                destination = "highPriorityLeads"
            )
            ManagementTile(
                title = "Customer Segment Insights",
                icon = R.drawable.ic_insights,
                navController = navController,
                destination = "customerInsights"
            )
        }

        Section(title = "Automations & Notifications") {
            ManagementTile(
                title = "All Automations",
                icon = R.drawable.ic_automations,
                navController = navController,
                destination = "allAutomations"
            )
            ManagementTile(
                title = "All Notifications",
                icon = R.drawable.ic_notifications,
                navController = navController,
                destination = "allNotifications"
            )
        }

        Section(title = "Tasks Overview") {
            ManagementTile(
                title = "Tasks Dashboard",
                icon = R.drawable.ic_dashboard,
                navController = navController,
                destination = "tasksDashboard"
            )
            ManagementTile(
                title = "Upcoming Tasks",
                icon = R.drawable.ic_upcoming_tasks,
                navController = navController,
                destination = "upcomingTasks"
            )
            ManagementTile(
                title = "Completed Tasks",
                icon = R.drawable.ic_completed_tasks,
                navController = navController,
                destination = "completedTasks"
            )
        }

        Section(title = "Reviews & Feedback") {
            ManagementTile(
                title = "Reviews List",
                icon = R.drawable.ic_reviews,
                navController = navController,
                destination = "reviewsList"
            )
            ManagementTile(
                title = "Reply to Reviews",
                icon = R.drawable.ic_reply_reviews,
                navController = navController,
                destination = "replyReviews"
            )
            ManagementTile(
                title = "Launch a Survey",
                icon = R.drawable.ic_launch_survey,
                navController = navController,
                destination = "launchSurvey"
            )
            ManagementTile(
                title = "Visualize Survey Results",
                icon = R.drawable.ic_survey_results,
                navController = navController,
                destination = "surveyResults"
            )
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
    icon: Int,
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
            painter = painterResource(id = R.drawable.ic_campaign),
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

@Preview
@Composable
fun ManagementViewPreview() {
    ManagementView(rememberNavController())
}
