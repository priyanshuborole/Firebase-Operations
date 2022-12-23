package com.application.firebaseoperations.ui.home

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.application.firebaseoperations.MediaPlayerFlow
import com.application.firebaseoperations.R
import com.application.firebaseoperations.data.auth.FirebaseAuthSource
import com.application.firebaseoperations.databinding.FragmentHomeBinding
import com.application.firebaseoperations.repository.auth.AuthRepository
import com.application.firebaseoperations.utils.progressDialog
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HomeFragment : Fragment() {
    private var progressDialog: Dialog? = null
    private lateinit var binding: FragmentHomeBinding
    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        val mediaPlayerFlow = MediaPlayerFlow()

        binding.btnPlay.setOnClickListener {
            if (!mediaPlayerFlow.isPlaying()) {
                val cacheDir = requireContext().cacheDir
                val audioFile = File(cacheDir, "sample3.mp3")
                if (audioFile.exists()) {
                    val audioUri = Uri.fromFile(audioFile)
                    CoroutineScope(Dispatchers.Main).launch {
                        mediaPlayerFlow.play(requireContext(), audioUri).collect {
                        }
                    }
                } else {
                    saveAudioFileFromFirebaseToCache(mediaPlayerFlow, requireContext())
                }
            }
        }
        binding.btnResume.setOnClickListener {
            mediaPlayerFlow.resume()
        }
        binding.btnPause.setOnClickListener {
            mediaPlayerFlow.pause()
        }
        binding.btnStop.setOnClickListener {
            if (mediaPlayerFlow.isPlaying()){
                mediaPlayerFlow.stop()
            }
        }

    }

    private fun saveAudioFileFromFirebaseToCache(
        mediaPlayerFlow: MediaPlayerFlow,
        context: Context
    ) {
        val storage = FirebaseStorage.getInstance()
        val audioRef = storage.getReference("audio/sample3.mp3")
        val cacheDir = context.cacheDir
        val audioFile = File(cacheDir, "sample3.mp3")
        val audioUri = Uri.fromFile(audioFile)

        val progress = context.progressDialog()
        progress.show()
        audioRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            try {
                val fos = FileOutputStream(audioFile)
                fos.write(bytes)
                fos.close()
                progress.dismiss()
                CoroutineScope(Dispatchers.Main).launch {
                    mediaPlayerFlow.play(context, audioUri).collect {
                        Toast.makeText(requireContext(),"Be focused",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                progress.dismiss()
                e.printStackTrace()
            }
        }.addOnFailureListener { exception ->
            progress.dismiss()
            Log.e(TAG, "saveAudioFileFromFirebaseToCache: $exception")
        }
    }

}