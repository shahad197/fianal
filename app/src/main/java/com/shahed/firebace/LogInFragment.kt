package com.shahed.firebace

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment() {

    private lateinit var  email :EditText
    private lateinit var password :EditText
    private lateinit var sign :Button
    private lateinit var vieModel :LogInViewModel

    private lateinit var forget :Button
    private lateinit var auth :FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.log_in_fragment2, container, false)
        init(view)
        return view

    }

    private fun init(view: View){
        email = view.findViewById(R.id.EmailAddress)
        password = view.findViewById(R.id.TextPassword)
        sign = view.findViewById(R.id.signIn)
        forget=view.findViewById(R.id.Forget_pas)

    }
    private fun showToast (msg :String){
        Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vieModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        forget.setOnClickListener {
            val forget = Intent(activity,ForgetPassword::class.java)
            startActivity(forget)
        }


        sign.setOnClickListener {

            val Email = email.text.toString()
            val Password = password.text.toString()
            if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {
                showToast("enter email and pass")
            } else {
                auth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast("good job")
                            val fragment = Scaner()
                            activity?.let {
                                it.supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainerView,fragment)
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }else{
                            showToast("wrong")
                        }
                    }
            }

        }

    }

}