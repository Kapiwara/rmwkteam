package com.example.barberqueue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.barberqueue.databinding.ActivityCalendarManagementBinding
import com.example.barberqueue.db.Settings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CalendarManagementActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityCalendarManagementBinding

    private lateinit var value: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        value = Settings()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val ref = db.collection("CalendarManagement").document("settings")

        binding.textViewFurtherDays.text = "-"

        ref.get().addOnSuccessListener {
            value = it.toObject(Settings::class.java)!!
            binding.textViewFurtherDays.text = value.further_days.toString()
        }

        binding.imageViewPrev.setOnClickListener {
            if (value.further_days > 1){
                value.further_days = value.further_days.dec()
                refreshData(value)
            }else{
                Toast.makeText(this,"Can't set up lower value", Toast.LENGTH_LONG)
            }
        }

        binding.imageViewNext.setOnClickListener {
            if (value.further_days < 365){
                value.further_days = value.further_days.inc()
                refreshData(value)
            }else{
                Toast.makeText(this,"Can't set up higher value", Toast.LENGTH_LONG)
            }
        }

        binding.saveChangesBtn.setOnClickListener {
            saveChangesInDatabase(value)
        }
    }

    private fun refreshData(value: Settings){
        binding.textViewFurtherDays.text = value.further_days.toString()
    }

    private fun saveChangesInDatabase(value: Settings){
        db.collection("CalendarManagement").document("settings").update(mapOf("further_days" to value.further_days))
    }
}