package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityAdminPanelBinding
import com.example.barberqueue.interfaces.AdminEditContact
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
        binding.adminContact.setOnClickListener{
            openActivityEditContact()
        }

        binding.calendarMenagementBtn.setOnClickListener {
            openActivityCalendarMenagement()
        }
    }

    private fun openActivityEditContact() {
        val intent = Intent(this, AdminEditContact::class.java)
        startActivity(intent)
    }

    private fun openActivityCalendarMenagement() {
        val intent = Intent(this, CalendarManagementActivity::class.java)
        startActivity(intent)
    }
}