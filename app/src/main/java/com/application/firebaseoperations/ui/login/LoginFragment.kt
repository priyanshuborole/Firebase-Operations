package com.application.firebaseoperations.ui.login

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.application.firebaseoperations.R
import com.application.firebaseoperations.data.auth.FirebaseAuthSource
import com.application.firebaseoperations.databinding.FragmentLoginBinding
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.ui.signup.SignupViewModel
import com.application.firebaseoperations.ui.signup.SignupViewModelFactory
import com.application.firebaseoperations.utils.AuthState
import com.application.firebaseoperations.utils.progressDialog
import com.application.firebaseoperations.utils.toast
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var progressDialog : Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = context?.progressDialog()

        val firebaseAuthSource = FirebaseAuthSource()
        val authRepository = AuthRepository(firebaseAuthSource)
        val viewModelFactory = LoginViewModelFactory(authRepository)
        val loginViewModel = ViewModelProvider(this,viewModelFactory)[LoginViewModel::class.java]

        lifecycleScope.launch {
            loginViewModel.loginResult.observe(viewLifecycleOwner){ result ->
                when(result){
                    is AuthState.Loading -> {
                        progressDialog?.show()
                    }
                    is AuthState.Success -> {
                        progressDialog?.dismiss();
                        navigateToHome()
                    }
                    is AuthState.Error -> {
                        progressDialog?.dismiss();
                        context?.toast(result.message.toString())
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val email : String = binding.inputEmail.text.toString()
            val password : String = binding.inputPassword.text.toString()
            loginViewModel.loginWithEmail(email,password)
        }

        binding.linkSignup.setOnClickListener {
            navigateToSignup()
        }

    }

    private fun navigateToSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

}