package com.shahed.firebace.CompanyUI

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.R
import com.shahed.firebace.WelcomeActivities.WelcomeActivity
import com.shahed.firebace.views.MainActivity

class MainActivity : AppCompatActivity() {
    private lateinit var lin_logout: LinearLayout
    private lateinit var lin_cats: LinearLayout
    private lateinit var lin_events: LinearLayout
    private lateinit var ttt: TextView
    private lateinit var eventns: TextView
    private lateinit var reservations: TextView
    private lateinit var categories: TextView
    private lateinit var logout: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val type = Typeface.createFromAsset(getAssets(), "fonts/Exo-Regular.ttf")
        val type2 = Typeface.createFromAsset(getAssets(), "fonts/Capture_it.ttf")
        ttt = findViewById(R.id.ttt)
        eventns = findViewById(R.id.events)
        reservations = findViewById(R.id.reserve)
        categories = findViewById(R.id.categoreis)
        logout = findViewById(R.id.logout)
        ttt.setTypeface(type2)
        eventns.setTypeface(type)
        reservations.setTypeface(type)
        categories.setTypeface(type)
        logout.setTypeface(type)
        lin_logout = findViewById(R.id.lin_logout)
        lin_cats = findViewById(R.id.lin_cats)
        lin_events = findViewById(R.id.lin_events)

        lin_logout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            Firebase.auth.signOut()
            finish()
        }
        lin_cats.setOnClickListener {
            val intent = Intent(this, CategorieActivity::class.java)
            startActivity(intent)
        }
        lin_events.setOnClickListener {
            val intent = Intent(this, MyEventsActivity::class.java)
            startActivity(intent)
        }
    }
}