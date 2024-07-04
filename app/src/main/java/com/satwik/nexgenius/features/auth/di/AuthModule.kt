
package com.satwik.nexgenius.features.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.satwik.nexgenius.core.reverse_shell.Host
import com.satwik.nexgenius.features.auth.data.repository.AuthRepositoryImpl
import com.satwik.nexgenius.features.auth.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.net.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        userCollectionRef: CollectionReference
    ): AuthRepository = AuthRepositoryImpl(auth, userCollectionRef)

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesUserCollectionRef(): CollectionReference {
        return Firebase.firestore.collection("User")
    }
}