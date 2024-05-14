package com.satwik.nexgenius.features.auth.domain.usecase

import com.satwik.nexgenius.core.common.Resource
import com.satwik.nexgenius.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignupUsecase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(email:String, password:String, username:String) = flow{
        emit(Resource.Loading())
        try{
            emit(Resource.Success(repository.signup(
                username = username,
                email = email,
                password = password
            )))
        }
        catch (e:Exception){
            emit(Resource.Error(e.message?:"Something went wrong"))
            e.printStackTrace()
        }
    }
}