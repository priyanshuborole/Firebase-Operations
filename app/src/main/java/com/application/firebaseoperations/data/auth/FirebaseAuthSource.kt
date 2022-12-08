package com.application.firebaseoperations.data.auth

import com.application.firebaseoperations.utils.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    suspend fun loginWithEmail(email: String, password: String): AuthState<FirebaseUser> {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return AuthState.Success(result.user!!)
    }

    suspend fun signupWithEmail(email: String, password: String): AuthState<FirebaseUser> {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return AuthState.Success(result.user!!)
    }

    fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}