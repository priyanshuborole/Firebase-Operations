package com.application.firebaseoperations.ui.login

import android.widget.ViewSwitcher.ViewFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.firebaseoperations.repository.auth.AuthRepository

class LoginViewModelFactory(
    private val authRepository: AuthRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        }
        throw IllegalArgumentException("ViewModel not created")
    }
}