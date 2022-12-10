package com.application.firebaseoperations.ui.login

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.application.firebaseoperations.MainActivity
import com.application.firebaseoperations.R
import com.application.firebaseoperations.data.auth.FirebaseAuthSource
import com.application.firebaseoperations.databinding.FragmentLoginBinding
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.AuthState
import com.application.firebaseoperations.utils.progressDialog
import com.application.firebaseoperations.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var progressDialog: Dialog? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginViewModel : LoginViewModel

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // handle the response in result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResult(task)
        }
    }

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
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        initGoogleSignInClient()
        lifecycleScope.launch {
            loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
                when (result) {
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
            val email: String = binding.inputEmail.text.toString()
            val password: String = binding.inputPassword.text.toString()
            loginViewModel.loginWithEmail(email, password)
        }

        binding.btnGoogle.setOnClickListener {
            signInUsingGoogle()
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
    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                loginViewModel.signInWithGoogle(credential)
            }
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun signInUsingGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }


}