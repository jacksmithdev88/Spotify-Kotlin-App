package com.example.assessmentapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_splash_screen.*

class ActivitySplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("SWITCH", MODE_PRIVATE)
        val dark = sharedPreferences.getBoolean("switched", false)
        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        setContentView(R.layout.activity_splash_screen)
        logo.alpha = 0f
        logo.animate().setDuration(3000).alpha(1f).withEndAction {
            val intent = Intent(this, ActivityMain::class.java)
            startActivity(intent)
            finish()
        }
    }
}