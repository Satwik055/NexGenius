package com.satwik.nexgenius.features.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.satwik.nexgenius.core.naviagtion.model.Graph
import com.satwik.nexgenius.core.naviagtion.model.Screen
import com.satwik.nexgenius.features.auth.presentation.login_screen.LoginScreen
import com.satwik.nexgenius.features.auth.presentation.signup_screen.SignupScreen

fun NavGraphBuilder.authGraph(navController: NavController){
    navigation(
        route = Graph.Auth.route,
        startDestination = Screen.Signup.route
    ){
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
    }
}