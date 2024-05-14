package com.satwik.nexgenius.features.auth.presentation.state

data class AuthenticationState(
    val successfull:Boolean = false,
    val error: String = "",
    val isLoading:Boolean = false,
)