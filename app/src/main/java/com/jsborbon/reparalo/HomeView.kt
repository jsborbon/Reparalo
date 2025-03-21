package com.jsborbon.reparalo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.components.LatestReviews
import com.jsborbon.reparalo.components.PopoverMenu

@Composable
fun HomeView(navController: NavHostController) {
    var selectedIndex = 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            NavigationBottomBar(
                selectedIndex = selectedIndex,
                navController = navController
            )
        }
    ) { innerPadding ->
        HomeViewContent(innerPadding)
    }
}

@Composable
fun HomeViewContent(innerPadding: PaddingValues) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
        Column{
            Text(
                text="Welcome back!",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
              text =  "Manolo Ruíz",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "4 new notifications",
                style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold

            )
        }


            PopoverMenu()
        }

        com.jsborbon.reparalo.ChartView()

        LatestReviews()
    }
}

