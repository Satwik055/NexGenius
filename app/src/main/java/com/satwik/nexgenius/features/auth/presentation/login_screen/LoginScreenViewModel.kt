package com.satwik.nexgenius.features.auth.presentation.login_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.nexgenius.core.common.Resource
import com.satwik.nexgenius.features.auth.domain.usecase.LoginUsecase
import com.satwik.nexgenius.features.auth.presentation.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val loginUseCase: LoginUsecase,
) : ViewModel() {

    private val _emailAuthState = mutableStateOf(AuthenticationState())
    val emailAuthState: State<AuthenticationState> = _emailAuthState

    fun login(email:String, password:String){
        loginUseCase(email, password).onEach {result->
            when(result){
                is Resource.Error -> _emailAuthState.value = AuthenticationState(error = result.message.toString())
                is Resource.Loading -> _emailAuthState.value = AuthenticationState(isLoading = true)
                is Resource.Success -> _emailAuthState.value = AuthenticationState(successfull = true)
            }
        }.launchIn(viewModelScope)
    }
}