package com.application.firebaseoperations.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.AuthState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupViewModel(
    private val authRepository: AuthRepository
) : ViewModel(){

    var signupResult : MutableLiveData<AuthState<FirebaseUser>> = MutableLiveData()

    fun signupWithEmail(email: String, password: String, confirmPassword : String) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signupResult.value = AuthState.Error("All fields are necessary")
        }
        else if(password != confirmPassword){
            signupResult.value = AuthState.Error("Password and Confirm password are not same")
        }
        else {
            signupResult.value = AuthState.Loading(null)
            viewModelScope.launch{
                val login = withContext(Dispatchers.IO){
                    authRepository.signUpWithEmail(email, password)
                }
                signupResult.value = login
            }
        }
    }
}