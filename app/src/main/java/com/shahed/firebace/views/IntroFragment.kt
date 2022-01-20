package com.shahed.firebace.views

import android.content.Intent
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
import com.shahed.firebace.R
import com.shahed.firebace.databinding.FragmentIntroBinding

private const val TAG = "LoginFragment"

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
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

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToRegisterFragment())
        }
        binding.btnRegisterc.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToRegisterCFragment())
        }
        val type = Typeface.createFromAsset(activity!!.getAssets(), "fonts/Exo-Regular.ttf")
        val type2 = Typeface.createFromAsset(activity!!.getAssets(), "fonts/Capture_it.ttf")
        binding.eventName.setTypeface(type)
        binding.eventNames.setTypeface(type)
        binding.btnLogin.setTypeface(type2)
        binding.btnRegister.setTypeface(type2)
        binding.btnRegisterc.setTypeface(type2)


    }

}