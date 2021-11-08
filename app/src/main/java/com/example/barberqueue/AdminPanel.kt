package com.example.barberqueue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityAdminPanelBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminPanel : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val logoutBtn = findViewById<Button>(R.id.logout_admin_btn)
        binding.logoutAdminBtn.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }
    }
}