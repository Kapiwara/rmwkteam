package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityViewAppointmentBinding
import com.example.barberqueue.db.OrderForm
import java.util.ArrayList
import android.text.method.ScrollingMovementMethod


class ViewAppointment : AppCompatActivity() {
    private lateinit var binding: ActivityViewAppointmentBinding
    private var list: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val visit: OrderForm = intent.getSerializableExtra("order") as OrderForm
        var dateAndHour: String = visit.date + "\t\t\t" + visit.hour
        binding.viewDate.text = dateAndHour
        binding.viewPrice.text = visit.price.toString()
        val serviceList: ArrayList<SummaryViewModel>? = visit.services

        if (serviceList != null) {
            val lengthOfList: Int = serviceList.size
            var a = 0;
            while (a < lengthOfList) {
                list += serviceList.get(a).text + "\n"
                a++;
            }
        }
        binding.servicesView.text = list
        binding.servicesView.movementMethod = ScrollingMovementMethod()   //just in case
        binding.cancelButton.setOnClickListener {
            //usuwanie
            


            //czy na pewno dialog

            //przeniesienie do Dashboard
            openActivityDashboard()

        }


    }

    private fun openActivityDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
    }
}
