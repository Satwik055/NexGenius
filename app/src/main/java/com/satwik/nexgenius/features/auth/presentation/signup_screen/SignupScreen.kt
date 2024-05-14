package com.satwik.nexgenius.features.auth.presentation.signup_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.satwik.nexgenius.core.naviagtion.model.Graph
import com.satwik.nexgenius.core.naviagtion.model.Screen
import com.satwik.nexgenius.features.auth.presentation.state.AuthenticationState

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupScreenViewModel = hiltViewModel()
) {

    val emailAuthState = viewModel.emailAuthState.value
    val context = LocalContext.current

    when{
        emailAuthState.isLoading->
            CircularProgressIndicator()
        emailAuthState.error.isNotBlank() ->
            LaunchedEffect(Unit) {
                Toast.makeText(context,emailAuthState.error,Toast.LENGTH_SHORT).show()
            }
        emailAuthState.successfull->
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Home.route){
                    popUpTo(Graph.Auth.route) {inclusive=true}
                }
            }
    }

    Content(viewModel, navController, emailAuthState)

}

@Composable
internal fun Content(
    viewModel: SignupScreenViewModel,
    navController: NavController,
    emailAuthState:AuthenticationState
) {

    Column(
        modifier = Modifier
            .background(color = Color(0xFFD3D1D1))
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ){

        var username by remember { mutableStateOf(TextFieldValue("")) }
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }

        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "Signup", fontSize = 32.sp, fontWeight = FontWeight.SemiBold )

        Spacer(modifier = Modifier.weight(0.16f))

        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Name")},
            shape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Email")},
            shape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp)


        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(mask = '\u25CF'),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Password")},
            shape = RoundedCornerShape(topEnd = 15.dp, topStart = 15.dp)


        )

        Spacer(modifier = Modifier.weight(0.3f))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(50.dp)
            ,
            onClick = {
                viewModel.signup(
                    email = email.text,
                    password = password.text,
                    username = username.text
                )
            })
        {
            Text(text = "Submit", fontSize = 18.sp)

        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Have an account ? Login",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(bottom = 60.dp)
                .clickable { navController.navigate(Screen.Login.route) }
                .align(Alignment.CenterHorizontally)
        )
    }
}