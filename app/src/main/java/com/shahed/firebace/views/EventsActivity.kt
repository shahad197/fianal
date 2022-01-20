package com.shahed.firebace.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.R
import com.shahed.firebace.bind.AdapterClass
import com.shahed.firebace.bind.AdapterEventsClass
import com.shahed.firebace.network.model.Categorie
import com.shahed.firebace.network.model.Event

class EventsActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    val cats = ArrayList<Event>()

    private lateinit var backBtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activit_events)
        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        rv = findViewById(R.id.rv)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager
        var firestore: FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Events")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {

                        val id = document.id
                        val address = document.data["address"].toString()
                        val category = document.data["category"].toString()
                        val date = document.data["date"].toString()
                        val description = document.data["description"].toString()
                        val imageUrl = document.data["imageUrl"].toString()
                        val lng = document.data["lng"].toString()
                        val lat = document.data["lat"].toString()
                        val userID = document.data["userID"].toString()
                        val title = document.data["title"].toString()


                        val Event = Event(
                            id = id,
                            address = address,
                            category = category,
                            date = date,
                            description = description,
                            imageUrl = imageUrl,
                            lng = lng,
                            lat = lat,
                            userID = userID,
                            title = title
                        )
                        val extras = intent.extras
                        if (extras != null) {
                            val value = extras.getString("Cat")
                            if (document.data["category"].toString().equals(value)) {
                                cats.add(Event)
                            }
                        } else {
                            cats.add(Event)
                        }
                    }
                    val adapter = AdapterEventsClass(this, cats)
                    rv.adapter = adapter
                }
            }


    }
}