package com.example.notesapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        Handler(Looper.getMainLooper()).postDelayed({
                   val intent = Intent(this@SplashActivity,NoteActivity::class.java)
            startActivity(intent)
            finish()
        },2500)
    }
}