package com.application.firebaseoperations.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.firebaseoperations.repository.auth.AuthRepository
import kotlinx.coroutines.CoroutineExceptionHandler

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var isUser: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        val currentUser = authRepository.currentUser()
        Log.i(TAG, "getCurrentUser: ${currentUser?.email.toString()}")
        isUser.value = currentUser != null
    }
}