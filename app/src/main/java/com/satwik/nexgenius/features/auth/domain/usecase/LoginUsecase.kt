package com.satwik.nexgenius.features.auth.domain.usecase

import com.satwik.nexgenius.core.common.Resource
import com.satwik.nexgenius.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUsecase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(email:String, password:String) = flow{
        emit(Resource.Loading())
        try{
            emit(Resource.Success(repository.login(email, password)))
        }
        catch (e:Exception){
            emit(Resource.Error(e.localizedMessage?:"Something went wrong"))
            e.printStackTrace()
        }
    }
}