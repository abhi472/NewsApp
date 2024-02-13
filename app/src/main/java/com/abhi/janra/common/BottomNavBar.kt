package com.abhi.janra.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abhi.alleimageviewer.states.Screens
import com.abhi.janra.screens.PictureScreen

@Composable
fun BottomNavigationBar(viewModel: MainViewModel) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Pictures.route,
            modifier = Modifier.padding(paddingValues = paddingValues)) {
            composable(Screens.Pictures.route) {
                PictureScreen(
                    viewModel,
                    navController
                )
            }

        }
    }
}