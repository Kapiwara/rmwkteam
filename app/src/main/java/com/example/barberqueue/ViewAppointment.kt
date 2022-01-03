package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityViewAppointmentBinding
import com.example.barberqueue.db.OrderForm
import java.util.ArrayList
import android.text.method.ScrollingMovementMethod
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.ceil


class ViewAppointment : AppCompatActivity() {
    private lateinit var binding: ActivityViewAppointmentBinding
    private var list: String = ""
    var selectedHourId: Int = 0
    var additionalHoursAmount: Int = 0
    val hoursList = listOf("8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00")

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
            

            //odznaczenie godzin, teraz jako wolne
            for ((id, value) in hoursList.withIndex()){
                if (value == visit.hour){
                    selectedHourId = id
                }
            }
            if (visit.servicesTime != null) {
                if (visit.servicesTime > 30){
                    additionalHoursAmount = ceil(visit.servicesTime.div(30).toFloat()).toInt()
                }
            }
            FirebaseDatabase.getInstance().getReference("HourStatus").child(visit.date.toString().replace('.','_')).child(
                selectedHourId.toString()
            ).setValue(true)
            if (additionalHoursAmount > 0){
                for (i in 1 until  additionalHoursAmount){
                    if (selectedHourId+i < 21){
                        FirebaseDatabase.getInstance().getReference("HourStatus").child(visit.date.toString().replace('.','_')).child(
                            (selectedHourId+i).toString()
                        ).setValue(true)
                    }
                }
            }

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
