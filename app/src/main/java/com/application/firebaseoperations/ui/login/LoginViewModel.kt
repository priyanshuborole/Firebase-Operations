package com.application.firebaseoperations.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.AuthState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var loginResult: MutableLiveData<AuthState<FirebaseUser>> = MutableLiveData()

    fun loginWithEmail(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            loginResult.value = AuthState.Error("All fields are necessary")
        } else {
            loginResult.value = AuthState.Loading(null)
            viewModelScope.launch{
                val login = withContext(Dispatchers.IO){
                    authRepository.loginWithEmail(email, password)
                }
                loginResult.value = login
            }
        }
    }
}