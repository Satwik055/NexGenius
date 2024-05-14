package com.satwik.nexgenius.features.home.presentation

import androidx.lifecycle.ViewModel
import com.satwik.nexgenius.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomescreenViewmodel @Inject constructor(
    private val authRepository: AuthRepository
):ViewModel() {

    fun logout(){
        authRepository.logout()
    }
}