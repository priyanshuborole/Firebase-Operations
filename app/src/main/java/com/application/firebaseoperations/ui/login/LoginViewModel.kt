package com.application.firebaseoperations.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.AuthState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*

private const val TAG = "LoginViewModel"
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var loginResult: MutableLiveData<AuthState<FirebaseUser>> = MutableLiveData()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        loginResult.postValue(AuthState.Error(exception.message.toString()))
        Log.e(TAG, "CoroutineExceptionHandler : Auth State Error ${exception.message.toString()}")
    }

    fun loginWithEmail(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            loginResult.value = AuthState.Error("All fields are necessary")
        } else {
            loginResult.value = AuthState.Loading(null)

            viewModelScope.launch(exceptionHandler) {
                supervisorScope {
                    val login = withContext(Dispatchers.IO) {
                        authRepository.loginWithEmail(email, password)
                    }
                    loginResult.value = login
                }
            }

        }
    }
}