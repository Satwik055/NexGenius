package com.satwik.nexgenius.features.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.satwik.nexgenius.core.common.User
import com.satwik.nexgenius.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth:FirebaseAuth,
    private val userCollectionRef: CollectionReference
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email,password).await()
    }

    override suspend fun signup(username: String, email: String, password: String) {

        //Creates firebase user
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user

        //Creates user in firestore
        firebaseUser?.let {
            val currentUid = it.uid
            val currentEmail = it.email
            val user = User(currentUid,username, currentEmail!!)
            userCollectionRef.document(currentUid).set(user)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}