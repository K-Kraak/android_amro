package com.amro.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.amro.app.navigation.AmroNavHost
import com.amro.core.designsystem.theme.AmroTheme

@Composable
fun AmroApp() {
    AmroTheme {
        AmroNavHost(navController = rememberNavController())
    }
}
