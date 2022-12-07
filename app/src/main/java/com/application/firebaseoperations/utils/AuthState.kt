package com.application.firebaseoperations.utils

sealed class AuthState<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : AuthState<T>(data)
    class Error<T>(message: String, data: T? = null) : AuthState<T>(data, message)
    class Loading<T>(data: T? = null) : AuthState<T>(data)
}