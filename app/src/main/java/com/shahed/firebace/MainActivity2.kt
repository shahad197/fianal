package com.shahed.firebace

import EditProfileActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import showP

const val request_code = 0
const val profile_KEY = "map"
class MainActivity2 : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        setContentView(R.layout.activity_main)


        saveData.setOnClickListener {

            val f1 = name.text.toString()
            val lastN =lastName.text.toString()
            val Age = etAge.text.toString()
                .toInt()

            val person = Person(f1,lastN,Age)
            savePerson(person)
        }

        profile.setOnClickListener {
            val intent = Intent(this,EditProfileActivity::class.java)
            intent.putExtra(showP,true)
            startActivityForResult(intent, request_code)
        }

        fun setAnswerShowenResult(){
            val date =Intent().apply {
                putExtra(profile_KEY,true)
                setResult(request_code)
            }
        }


    }
    private fun savePerson(person: Person) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val personCollection = Firebase.firestore.collection("persons")
            personCollection.add(person).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity2, "successfuly data", Toast.LENGTH_LONG).show()
            }


        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity2, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}