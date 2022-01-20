package com.shahed.firebace.WelcomeActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.shahed.firebace.R
import com.shahed.firebace.views.MainActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var skip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        skip = findViewById(R.id.skip)

        skip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}