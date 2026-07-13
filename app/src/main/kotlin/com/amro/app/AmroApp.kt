package com.amro.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.amro.app.navigation.AppNavHost

@Composable
fun AmroApp() {
    val navController = rememberNavController()
    AppNavHost(navController)
}