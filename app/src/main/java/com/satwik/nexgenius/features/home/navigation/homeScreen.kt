package com.satwik.nexgenius.features.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.satwik.nexgenius.core.naviagtion.model.Screen
import com.satwik.nexgenius.features.home.presentation.HomeScreen

fun NavGraphBuilder.homeScreen(navController: NavController){
    composable(
        route = Screen.Home.route,
    ) {
        HomeScreen(navController = navController)
    }
}