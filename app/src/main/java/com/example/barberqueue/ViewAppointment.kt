package com.example.barberqueue

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityMainBinding
import com.example.barberqueue.databinding.ActivityViewAppointmentBinding
import com.example.barberqueue.db.OrderForm

class ViewAppointment : AppCompatActivity() {
    private lateinit var binding: ActivityViewAppointmentBinding
    private lateinit var order: OrderForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
            //order = intent.getSerializableExtra("order") as OrderForm;
        //binding.cancelButton.setOnClickListener();
    }


}
