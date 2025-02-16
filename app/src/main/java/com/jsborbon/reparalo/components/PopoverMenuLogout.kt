package com.jsborbon.relato.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.relato.R

@Composable
fun PopoverMenu() {
    var menuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .clickable { menuExpanded = !menuExpanded }
    ) {
        Image(
            painter = painterResource(id = R.drawable.companyicon),
            contentDescription = "Company Icon",
            modifier = Modifier.size(70.dp),
            contentScale = ContentScale.Fit
        )
    }

    if (menuExpanded) {
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .width(150.dp)
                .background(Color.White)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            menuExpanded = false
                            FirebaseAuth.getInstance().signOut()
                        }
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
            }
        }
    }
}