package com.satwik.nexgenius.core.naviagtion.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.satwik.nexgenius.core.naviagtion.model.Graph
import com.satwik.nexgenius.core.naviagtion.model.Screen
import com.satwik.nexgenius.features.auth.navigation.authGraph
import com.satwik.nexgenius.features.home.navigation.homeScreen


@Composable
fun SetupNavGraph(
    navController: NavHostController,
) {
    var startDestination by remember { mutableStateOf("") }

    startDestination = Firebase.auth.currentUser?.let { Screen.Home.route } ?: Graph.Auth.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        homeScreen(navController)
    }
}