package com.example.barberqueue

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityMainBinding
import com.example.barberqueue.databinding.ActivityViewAppointmentBinding

class ViewAppointment : AppCompatActivity() {
    private lateinit var binding: ActivityViewAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.cancelButton.setOnClickListener();
    }


}
