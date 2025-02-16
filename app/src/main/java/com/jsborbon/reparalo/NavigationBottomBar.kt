package com.jsborbon.relato

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun NavigationBottomBar(selectedIndex:Int, navController: NavController) {
    NavigationBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomBarIcon(
                selected = selectedIndex == 0,
                onClick = {
                    if(selectedIndex != 0){
                    navController.navigate(Routes.Home)
                }},
                icon = R.drawable.ic_home,
                contentDescription = "Home"
            )
            BottomBarIcon(
                selected = selectedIndex == 1,
                onClick = {
                    if(selectedIndex != 1){
                    navController.navigate(Routes.Calendar) }},
                icon = R.drawable.ic_calendar,
                contentDescription = "Calendar"
            )
            BottomBarIcon(
                selected = selectedIndex == 2,
                onClick = {
                    if(selectedIndex != 2){
                    navController.navigate(Routes.Management) }},
                icon = R.drawable.ic_mail_and_text_magnifyingglass,
                contentDescription = "Management"
            )
            BottomBarIcon(
                selected = selectedIndex == 3,
                onClick = {
                    if(selectedIndex != 3){
                    navController.navigate(Routes.Communications) }},
                icon = R.drawable.ic_mailbox,
                contentDescription = "Mailbox"
            )
        }
    }
}
@Composable
fun BottomBarIcon(
    selected: Boolean,
    onClick: () -> Unit,
    icon: Int,
    contentDescription: String
) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(24.dp)
            .clickable { onClick() },
        tint = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
    )
}

