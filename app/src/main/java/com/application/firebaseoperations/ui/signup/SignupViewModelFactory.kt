package com.application.firebaseoperations.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.firebaseoperations.repository.auth.AuthRepository

class SignupViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(authRepository) as T
        }
        throw IllegalArgumentException("ViewModel not created")
    }
}