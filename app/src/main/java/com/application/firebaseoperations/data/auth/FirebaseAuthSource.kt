package com.application.firebaseoperations.data.auth

import com.application.firebaseoperations.utils.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthSource {
    private val firebaseAuth : FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    suspend fun loginWithEmail(email: String, password: String) : AuthState<FirebaseUser>{
        return  try {
            val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
            AuthState.Success(result.user!!)
        }
        catch (e : Exception){
            AuthState.Error("Incorrect email or password")
        }
    }

     suspend fun signupWithEmail(email: String, password: String): AuthState<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            return AuthState.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            AuthState.Error(e.message.toString())
        }
    }

    suspend fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}