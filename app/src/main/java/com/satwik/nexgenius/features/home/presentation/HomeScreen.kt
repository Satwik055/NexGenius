package com.satwik.nexgenius.features.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.satwik.nexgenius.core.naviagtion.model.Graph
import com.satwik.nexgenius.core.naviagtion.model.Screen
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomescreenViewmodel = hiltViewModel()
) {

    Content(
        viewModel = viewModel,
        navController = navController
    )

}

@Composable
internal fun Content(viewModel: HomescreenViewmodel, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = "Home", modifier = Modifier.align(Alignment.TopCenter))

        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = {
                viewModel.logout()
                navController.navigate(Screen.Signup.route){
                    popUpTo(Screen.Home.route) {inclusive=true}
                }
            })
        {
            Text(text = "Logout")
        }
    }
}