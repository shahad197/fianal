package com.shahed.firebace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class ForgetPassword : AppCompatActivity() {
    lateinit var etEmail: EditText
    private var mAuth: FirebaseAuth? = null
    private lateinit var btn_forget :Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        viewInitializations()
        mAuth = FirebaseAuth.getInstance()

        btn_forget = findViewById(R.id.bt_forget)

        btn_forget.setOnClickListener {

            performForgetPassword()
        }

    }


    fun viewInitializations() {

        etEmail = findViewById(R.id.et_email)

        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    fun validateInput(): Boolean {

        if (etEmail.text.toString().equals("")) {
            etEmail.setError("Please Enter Email")
            return false
        }
        // checking the proper email format
        if (!isEmailValid(etEmail.text.toString())) {
            etEmail.setError("Please Enter Valid Email")
            return false
        }
        return true
    }

    fun isEmailValid(email: String): Boolean {
        return EMAIL_ADDRESS.matcher(email).matches()
    }

    fun performForgetPassword () {
        if (validateInput()) {
            Toast.makeText(this,"here",Toast.LENGTH_SHORT).show()

            // Input is valid, here send data to your server


            val email = etEmail.text.toString()

            mAuth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // successful!
                        Toast.makeText(this,"Email send to Register Email Address", Toast.LENGTH_SHORT).show()
                    } else {
                        // failed!
                    }
                }



            // Here you can call you API

        }
    }



}