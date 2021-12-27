package com.shahed.firebace

import EditProfileActivity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.register_fragment.*
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.firestore.DocumentReference

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import showP


const val TAG = "supmet"
class RegesterFragment : Fragment() {

    private lateinit var  usserName : EditText
    private lateinit var email : EditText
    private lateinit var passwored : EditText
    private lateinit var  conformpass : EditText
    private lateinit var supmetRegester : Button
    private lateinit var logIn :Button
  //  private lateinit var profilet :Button

   private lateinit var authe : FirebaseAuth
   private lateinit var firestore :FirebaseFirestore


    private val viewModel: RegisterViewModel by lazy { ViewModelProvider(this)[RegisterViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authe = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

    }

    override fun onStart() {
        super.onStart()
        val currentUser = authe.currentUser
        if (currentUser != null)
            Log.d(TAG, "hi${currentUser.displayName}")


        logIn.setOnClickListener {

            val fragment = LogInFragment()
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView,fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }


        supmetRegester.setOnClickListener {
            val username: String = usserName.text.toString()
            val email: String = email.text.toString()
            val password: String = passwored.text.toString()
            val confirmPassword = conformpass.text.toString()

            val fragment = Scaner()
            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView,fragment)
                    .addToBackStack(null)
                    .commit()}


            when {
                username.isEmpty() -> showToast("plz enter a username")
                email.isEmpty() -> showToast("plz enter an email")
                password.isEmpty() -> showToast("plz enter password")
                password != confirmPassword -> showToast("passwords must be matched")
                else -> {
                    registerUser(username, email.trim(), password)

                }
            }
        }
    }

    fun registerUser (userName : String, email :String,password :String) {
        Log.e("emaill" , email)
        authe.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("good job")



                    val intent = Intent(activity,MainActivity2::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "there is proplem", task.exception)
                }

            }
        val updateProfile = userProfileChangeRequest {
            displayName = userName
        }
        authe.currentUser?.updateProfile(updateProfile)
    }




         fun showToast(msg:String){
            Toast.makeText( requireContext(), msg  , Toast.LENGTH_LONG).show()

        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val v = inflater.inflate(R.layout.register_fragment, container, false)
            init(v)
            return v
        }


    private fun init (v:View){
        usserName = v.findViewById(R.id.username)
        email = v .findViewById(R.id.email)
        passwored = v.findViewById(R.id.pass)
        conformpass = v.findViewById(R.id.conformPassword)
        supmetRegester = v.findViewById(R.id.regester)
        logIn = v.findViewById(R.id.LogInn)
       // profilet = v.findViewById(R.id.profile)

    }




}