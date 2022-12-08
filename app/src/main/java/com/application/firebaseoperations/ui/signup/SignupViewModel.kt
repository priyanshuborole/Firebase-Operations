package com.application.firebaseoperations.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.AuthState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*

private const val TAG = "SignupViewModel"

class SignupViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var signupResult: MutableLiveData<AuthState<FirebaseUser>> = MutableLiveData()
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        signupResult.postValue(AuthState.Error(exception.message.toString()))
        Log.e(TAG, "CoroutineExceptionHandler : Auth State Error ${exception.message.toString()}")
    }

    fun signupWithEmail(email: String, password: String, confirmPassword: String) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signupResult.value = AuthState.Error("All fields are necessary")
        } else if (password != confirmPassword) {
            signupResult.value = AuthState.Error("Password and Confirm password are not same")
        } else {
            signupResult.value = AuthState.Loading(null)

            viewModelScope.launch(exceptionHandler) {
                supervisorScope {
                    val login = withContext(Dispatchers.IO) {
                        authRepository.signUpWithEmail(email, password)
                    }
                    signupResult.value = login
                }
            }

        }
    }
}