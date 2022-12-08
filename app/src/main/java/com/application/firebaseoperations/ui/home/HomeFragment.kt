package com.application.firebaseoperations.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.application.firebaseoperations.R
import com.application.firebaseoperations.data.auth.FirebaseAuthSource
import com.application.firebaseoperations.databinding.FragmentHomeBinding
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.progressDialog

class HomeFragment : Fragment() {
    private var progressDialog: Dialog? = null
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        progressDialog = context?.progressDialog()

        val firebaseAuthSource = FirebaseAuthSource()
        val authRepository = AuthRepository(firebaseAuthSource)
        val viewModelFactory = HomeViewModelFactory(authRepository)
        val homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        homeViewModel.isUser.observe(viewLifecycleOwner) { isUser ->
            if (!isUser) {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}