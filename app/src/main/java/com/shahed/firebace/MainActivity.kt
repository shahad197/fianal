package com.shahed.firebace

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        saveData.setOnClickListener {

            val f1 = name.text.toString()
            val lastN =lastName.text.toString()
            val Age = etAge.text.toString().toInt()

            val person = Person(f1,lastN,Age)
            savePerson(person)

        }

    }


  private fun savePerson(person: Person) = CoroutineScope(Dispatchers.IO).launch {
      try {
          val personCollection = Firebase.firestore.collection("persons")
          personCollection.add(person).await()
          withContext(Dispatchers.Main) {
              Toast.makeText(this@MainActivity, "successfuly data", Toast.LENGTH_LONG).show()
          }


      } catch (e: Exception) {
          withContext(Dispatchers.Main) {
              Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
          }
      }
  }
}