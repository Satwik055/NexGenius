package com.satwik.nexgenius.features.auth.presentation.signup_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.nexgenius.core.common.Resource
import com.satwik.nexgenius.features.auth.domain.usecase.SignupUsecase
import com.satwik.nexgenius.features.auth.presentation.state.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SignupScreenViewModel @Inject constructor(
    private  val signupUseCase: SignupUsecase,
) : ViewModel() {

    private val _emailAuthState = mutableStateOf(AuthenticationState())
    val emailAuthState: State<AuthenticationState> = _emailAuthState

    fun signup(email:String, password:String, username:String){
        signupUseCase(email = email, password = password, username = username).onEach { result->
            when(result){
                is Resource.Error -> _emailAuthState.value = AuthenticationState(error = result.message.toString())
                is Resource.Loading -> _emailAuthState.value = AuthenticationState(isLoading = true)
                is Resource.Success -> _emailAuthState.value = AuthenticationState(successfull = true)
            }
        }.launchIn(viewModelScope)
    }
}