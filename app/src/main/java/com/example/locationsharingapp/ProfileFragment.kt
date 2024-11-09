package com.example.locationsharingapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharingapp.View.Loginactivity
import com.example.locationsharingapp.View.MainActivity
import com.example.locationsharingapp.ViewModel.AuthenticationViewModel
import com.example.locationsharingapp.ViewModel.FirestoreViewModel
import com.example.locationsharingapp.ViewModel.Location
import com.example.locationsharingapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var authViewModel: AuthenticationViewModel
    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var locationViewModel: Location
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        authViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)
        locationViewModel = ViewModelProvider(this).get(Location::class.java)


        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(requireContext(), Loginactivity::class.java))

        }

        binding.homeBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        loadUserInfo()
        binding.updateBtn.setOnClickListener {
            val newName = binding.NameEt.text.toString()
            val newLocation = binding.Loaction.text.toString()

            updateBtn(newName, newLocation)
        }

        return binding.root
    }


    private fun updateBtn(newName: String, newLocation: String) {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            val userId = currentUser.uid
            firestoreViewModel.updateUser(requireContext(), userId, newName, newLocation)
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), MainActivity::class.java))
        } else {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserInfo() {
        val currentUser = authViewModel.getCurrentUser()
        if(currentUser != null) {
            binding.emailEt.setText(currentUser.email)
            firestoreViewModel.getUser(requireContext(), currentUser.uid){ user ->
                if (currentUser.displayName != null) {
                    binding.NameEt.setText(currentUser.displayName)
                }
            }
        }else {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
        }

    }
}