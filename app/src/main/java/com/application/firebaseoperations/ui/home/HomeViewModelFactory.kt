package com.application.firebaseoperations.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.firebaseoperations.repository.auth.AuthRepository

class HomeViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(authRepository) as T
        }
        throw IllegalArgumentException("ViewModel not created")
    }
}