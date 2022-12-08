package com.application.firebaseoperations.repository.auth

import com.application.firebaseoperations.data.auth.FirebaseAuthSource
import com.application.firebaseoperations.utils.AuthState
import com.google.firebase.auth.FirebaseUser

class AuthRepository(
    private val firebaseAuthSource: FirebaseAuthSource
) {

    suspend fun loginWithEmail(email: String, password: String): AuthState<FirebaseUser> =
        firebaseAuthSource.loginWithEmail(email, password)

    suspend fun signUpWithEmail(email: String, password: String): AuthState<FirebaseUser> =
        firebaseAuthSource.signupWithEmail(email, password)

    fun currentUser(): FirebaseUser? = firebaseAuthSource.currentUser()

}