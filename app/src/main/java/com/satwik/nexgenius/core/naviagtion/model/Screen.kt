package com.satwik.nexgenius.core.naviagtion.model

sealed class Screen(val route:String) {
    object Signup: Screen(route = "signup_screen")
    object Login: Screen(route = "login_screen")
    object Home: Screen(route = "home_screen")

}
