package com.example.barberqueue

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityViewAppointmentBinding
import com.example.barberqueue.db.OrderForm
import java.util.ArrayList
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Toast
import com.example.barberqueue.databinding.AdminviewAppointmentBinding
import com.example.barberqueue.db.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlin.math.ceil


class ViewAppointmentAdmin : AppCompatActivity() {
    private lateinit var binding: AdminviewAppointmentBinding
    private var list: String = ""
    var selectedHourId: Int = 0
    var additionalHoursAmount: Int = 0
    val hoursList = listOf(
        "8:00",
        "8:30",
        "9:00",
        "9:30",
        "10:00",
        "10:30",
        "11:00",
        "11:30",
        "12:00",
        "12:30",
        "13:00",
        "13:30",
        "14:00",
        "14:30",
        "15:00",
        "15:30",
        "16:00",
        "16:30",
        "17:00",
        "17:30",
        "18:00"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminviewAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var visit: OrderForm = intent.getSerializableExtra("order") as OrderForm
        val visitid: String = intent.getStringExtra("id") as String
        var dateAndHour: String = visit.date + "\t\t\t" + visit.hour

        getClientData(visit.userId.toString())
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
            visit.isCanceled = true
            FirebaseDatabase.getInstance().getReference("FutureAppointment").child(visitid).child("canceled").setValue(true)

            //odznaczenie godzin, teraz jako wolne
            // sprawdzenie id wizyty w tablicy
            for ((id, value) in hoursList.withIndex()) {
                if (value == visit.hour) {
                    selectedHourId = id
                }
            }
            // sprawdzenie ile następnych godzin należy zwolnić
            if (visit.servicesTime != null) {
                if (visit.servicesTime > 30) {
                    additionalHoursAmount = ceil(visit.servicesTime.div(30).toFloat()).toInt()
                }
            }
            // zwalnianie godzin
            FirebaseDatabase.getInstance().getReference("HourStatus")
                .child(visit.date.toString().replace('.', '_')).child(
                    selectedHourId.toString()
                ).setValue(true)
            if (additionalHoursAmount > 0) {
                for (i in 1 until additionalHoursAmount) {
                    if (selectedHourId + i < 21) {
                        FirebaseDatabase.getInstance().getReference("HourStatus")
                            .child(visit.date.toString().replace('.', '_')).child(
                                (selectedHourId + i).toString()
                            ).setValue(true)
                    }
                }


            }
            //czy na pewno dialog

            //przeniesienie do Dashboard
            openActivityDashboard()

        }

        // oznaczanie wizyty jako zaakceptowanej
        binding.confirmButton.setOnClickListener {
            visit.isAccepted = true
            FirebaseDatabase.getInstance().getReference("FutureAppointment").child(visitid).child("accepted").setValue(true)
            Toast.makeText(this, "Visit confirmed successfully.", Toast.LENGTH_SHORT).show()
            openActivityCalendarManagementActivity()
        }


    }

    // pobranie danych o kliencie
    private fun getClientData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").document(userId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

                var client = snapshot.toObject<User>()
                var clientData: String =
                    client?.name.toString() + "\n" + client?.phone.toString() + "\n" + client?.login.toString()
                binding.clientData.text = clientData


            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    private fun openActivityDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
    }

    private fun openActivityCalendarManagementActivity() {
        val intent = Intent(this, CalendarManagementActivity::class.java)
        startActivity(intent)
    }
}
