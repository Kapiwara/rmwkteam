package com.example.barberqueue

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager




class MakeAppointment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_appointment)

        val calendar: Calendar = Calendar.getInstance()

        val priceSum = intent.getStringExtra("priceSum")?.toFloat()
        val timeSum = intent.getStringExtra("timeSum")?.toInt()
        var selectedDateTime: String = ""



        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            {_, _year, _month, _dayOfMonth ->
                val dateString = "$_dayOfMonth.$_month.$_year"
                selectedDateTime = dateString

            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = calendar.time.time
        datePickerDialog.show()

        val hoursList = listOf("8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00")


        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.hours_recycler_view)

        // this creates a vertical layout Manager
        val numberOfColumns = 4
        recyclerview.layoutManager = GridLayoutManager(this, numberOfColumns)
        // ArrayList of class ItemsViewModel
        val data = ArrayList<HoursViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for(i in hoursList){
            data.add(HoursViewModel(i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = HoursAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter


        findViewById<Button>(R.id.go_back_btn).setOnClickListener { openActivityNewVisit()}

    }

    private fun openActivityNewVisit() {
        val intent = Intent(this,NewVisit::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        openActivityNewVisit()

    }

}