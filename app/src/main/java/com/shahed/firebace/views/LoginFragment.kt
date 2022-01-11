package com.shahed.firebace.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.databinding.FragmentLoginBinding

private const val TAG = "LoginFragment"


class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logBtn.setOnClickListener() {
            email = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()
            if (validateInput(email, password)) loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    Toast.makeText(
                        requireContext(),
                        auth.currentUser?.displayName.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e(TAG, "there was something wrong", task.exception)
                }

            }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "Complete missing fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        } else {
            Toast.makeText(requireContext(), "Enter a valid email", Toast.LENGTH_SHORT).show()
        }

        return false
    }
}