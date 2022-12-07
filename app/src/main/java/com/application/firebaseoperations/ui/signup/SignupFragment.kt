package com.application.firebaseoperations.ui.signup

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
import com.application.firebaseoperations.databinding.FragmentSignupBinding
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.AuthState
import com.application.firebaseoperations.utils.progressDialog
import com.application.firebaseoperations.utils.toast
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = context?.progressDialog()

        val firebaseAuthSource = FirebaseAuthSource()
        val authRepository = AuthRepository(firebaseAuthSource)
        val viewModelFactory = SignupViewModelFactory(authRepository)
        val signupViewModel = ViewModelProvider(this,viewModelFactory)[SignupViewModel::class.java]

        lifecycleScope.launch {
            signupViewModel.signupResult.observe(viewLifecycleOwner){ result ->
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

        binding.btnSignup.setOnClickListener {
            val email : String = binding.inputEmail.text.toString()
            val password : String = binding.inputPassword.text.toString()
            val confirmPassword : String = binding.inputConfirmPassword.text.toString()
            signupViewModel.signupWithEmail(email,password,confirmPassword)
        }

        binding.linkLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
    }
    private fun navigateToHome() {
        findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
    }
}