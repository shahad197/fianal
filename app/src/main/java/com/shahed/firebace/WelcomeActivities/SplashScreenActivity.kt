package com.shahed.firebace.WelcomeActivities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.shahed.firebace.CompanyUI.MainActivity
import com.shahed.firebace.R
import com.shahed.firebace.utils.NotificationsService

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val service = Intent(this, NotificationsService::class.java)
        startService(service);

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({

            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()


        }, 3000) // 3000 is the delayed time in milliseconds.

    }
}