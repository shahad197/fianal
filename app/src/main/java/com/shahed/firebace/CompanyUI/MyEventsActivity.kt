package com.shahed.firebace.CompanyUI

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
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

class MyEventsActivity : AppCompatActivity() {
    private lateinit var rv: RecyclerView
    val cats = ArrayList<Event>()
    private lateinit var ty: TextView
    private lateinit var backBtn: ImageView
    private lateinit var floatingActionButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)
        backBtn = findViewById(R.id.backBtn)
        ty = findViewById(R.id.ty)
        val type = Typeface.createFromAsset(getAssets(), "fonts/Exo-Regular.ttf")
        val type2 = Typeface.createFromAsset(getAssets(), "fonts/Capture_it.ttf")
        ty.setTypeface(type2)
        floatingActionButton = findViewById(R.id.btn_add)
        backBtn.setOnClickListener {
            finish()
        }
        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            startActivity(intent)

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
                        if (userID.equals(Firebase.auth.currentUser?.uid)) {
                            cats.add(Event)
                            Log.e("IDDD", Event.id.toString())
                        }
                    }
                    val adapter = AdapterEventsClass(this, cats)
                    rv.adapter = adapter
                }
            }


    }
}