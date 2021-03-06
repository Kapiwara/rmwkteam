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

        // przyporządkowanie odpowiednich funkcji pod przyciski
        binding.logoutAdminBtn.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }
        binding.adminContact.setOnClickListener{
            openActivityEditContact()
        }
        binding.calendarMenagementBtn.setOnClickListener {
            openActivityCalendarManagement()
        }
        binding.adminStats.setOnClickListener{
            openActivityAdminStats()
        }
    }

    private fun openActivityAdminStats() {
        val intent = Intent(this, AdminStats::class.java)
        startActivity(intent)
    }

    private fun openActivityEditContact() {
        val intent = Intent(this, AdminEditContact::class.java)
        startActivity(intent)
    }

    private fun openActivityCalendarManagement() {
        val intent = Intent(this, CalendarManagementActivity::class.java)
        startActivity(intent)
    }
}