package com.jsborbon.reparalo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.navigation.NavigationWrapper
import com.jsborbon.reparalo.ui.theme.ReparaloTheme

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ReparaloTheme {
                navHostController = rememberNavController()
                auth = FirebaseAuth.getInstance()

                NavigationWrapper(navHostController, auth)
            }
        }
    }
}
