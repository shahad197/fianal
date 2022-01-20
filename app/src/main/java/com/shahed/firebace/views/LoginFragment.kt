package com.shahed.firebace.views

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.CompanyUI.MainActivity
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
        val type = Typeface.createFromAsset(activity!!.getAssets(), "fonts/Exo-Regular.ttf")
        val type2 = Typeface.createFromAsset(activity!!.getAssets(), "fonts/Capture_it.ttf")
        binding.texttitle.setTypeface(type)
        binding.forgotPassword.setTypeface(type)
        binding.skipTextView.setTypeface(type)
        binding.logBtn.setTypeface(type2)
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
                    var firestore: FirebaseFirestore
                    firestore = FirebaseFirestore.getInstance()
                    firestore.collection("users")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                Log.e("DataRef", "${document.data}")
                                Log.e("ID", "${document.data.get("id")}")
                                if ("${document.data.get("id")}".equals(Firebase.auth.currentUser?.uid)) {
                                    Log.e("UserType", "${document.data.get("userType")}")
                                    val sharedPreferences: SharedPreferences =
                                        activity!!.getSharedPreferences("Event App", 0)
                                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                    editor.putString("userType", "${document.data.get("userType")}")
                                    editor.putString("id", "${document.id}")
                                    editor.putString("img", "${document.data.get("imageUrl")}")
                                    editor.apply()
                                    editor.commit()
                                    if ("${document.data.get("userType")}".equals("Company")) {
                                        val intent = Intent(context, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                                        Toast.makeText(
                                            requireContext(),
                                            auth.currentUser?.displayName.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                            }

                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "No Data In DB", Toast.LENGTH_SHORT).show()
                        }


                } else {
                    Log.e(TAG, "there was something wrong", task.exception)
                    Toast.makeText(context, "You Don't Hace Account ! ", Toast.LENGTH_LONG).show()
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