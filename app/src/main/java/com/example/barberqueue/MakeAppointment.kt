package com.example.barberqueue

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.barberqueue.adapters.HoursAdapter
import com.example.barberqueue.databinding.ActivityMakeAppointmentBinding
import com.example.barberqueue.db.Settings
import com.example.barberqueue.interfaces.FromMakeAppointmentToSummary
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport


class MakeAppointment : AppCompatActivity(), FromMakeAppointmentToSummary {

    private lateinit var db: FirebaseFirestore
    private var listOfHours = ArrayList<HoursViewModel>()
    private lateinit var binding:ActivityMakeAppointmentBinding
    var adapter = HoursAdapter(listOfHours, this)
    private var selectedHour: String = "0:00"
    private lateinit var value: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakeAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        value = Settings()
        binding.selectedDateTextView.text = "          "

        val calendar: Calendar = Calendar.getInstance()
        val priceSum = intent.getStringExtra("priceSum")
        val timeSum = intent.getStringExtra("timeSum")
        val chosenServices = intent.getStringArrayExtra("chosenServices")
        var selectedDate: String = ""

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val ref = db.collection("CalendarManagement").document("settings")
        val ref1 = FirebaseDatabase.getInstance().getReference("HoursStatus").child("Future")

        ref.get().addOnSuccessListener {
            value = it.toObject(Settings::class.java)!!
        }

        val datePickerDialog = DatePickerDialog(
            this,
            {_, _year, _month, _dayOfMonth ->
                val dateString = _dayOfMonth.toString() + "." + (_month+1).toString() + "." + _year.toString()
                selectedDate = dateString
                binding.selectedDateTextView.text = selectedDate
                //updateHours(selectedDate)

            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = calendar.time.time

        ref.get().addOnSuccessListener {
            value = it.toObject(Settings::class.java)!!
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis + (value.further_days * 24 * 60 * 60 * 1000)
            datePickerDialog.show()
        }


        val hoursList = listOf("8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00")

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.hours_recycler_view)

        // this creates a vertical layout Manager
        val numberOfColumns = 4
        recyclerview.layoutManager = GridLayoutManager(this, numberOfColumns)
        // ArrayList of class ItemsViewModel

        // This loop will create 20 Views containing
        // the image with the count of view
        for(i in hoursList){
            listOfHours.add(HoursViewModel(i))
        }

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter




        PropertyChangeSupport(selectedDate).addPropertyChangeListener {

        }

        binding.currentData.setOnClickListener { datePickerDialog.show()}
        binding.goBackBtn.setOnClickListener { openActivityNewVisit()}
        binding.selectDateBtn.setOnClickListener{

            val intent = Intent (this@MakeAppointment,SummaryActivity::class.java)
            intent.putExtra("chosenServices", chosenServices)
            intent.putExtra("priceSum", priceSum)
            intent.putExtra("timeSum", timeSum)
            intent.putExtra("selectedDate", "$selectedDate")
            intent.putExtra("selectedHour", "$selectedHour")

            startActivity(intent)
        }
    }

    private fun openActivityNewVisit() {
        val intent = Intent(this,NewVisit::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        openActivityNewVisit()

    }

    override fun getSelectedTime(time: String) {
        selectedHour = time
    }

    fun updateHours(date: String){
        val ref = FirebaseDatabase.getInstance().getReference("HoursStatus").child("Future").child(date)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfHours.clear()
                if (dataSnapshot.exists()){
                    for (i in dataSnapshot.children){
                        val value = i.getValue(HoursViewModel::class.java)
                        if (value != null && value.free == true) {
                            listOfHours.add(value)
                        }
                    }
                }else{
                    val hoursList = listOf("8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00")
                    for(i in hoursList){
                        listOfHours.add(HoursViewModel(i))
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
    }

}