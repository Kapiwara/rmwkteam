package com.example.barberqueue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
        val logoutBtn = findViewById<Button>(R.id.logout_admin_btn)
        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }
    }
}