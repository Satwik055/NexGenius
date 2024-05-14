package com.satwik.nexgenius.features.auth.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun signup(username:String,email: String, password: String)
    fun logout()
}