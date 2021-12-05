package com.example.barberqueue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.adapters.HoursAdapter
import com.example.barberqueue.adapters.SummaryAdapter
import java.util.ArrayList

private var listOfSummaryServices = ArrayList<SummaryViewModel>()
var adapter = SummaryAdapter(listOfSummaryServices)

class SummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val priceSum = intent.getStringExtra("priceSum")
        val timeSum = intent.getStringExtra("timeSum")
        val chosenServices = intent.getStringArrayExtra("chosenServices")
        val selectedDateTime = intent.getStringExtra("selectedDateTime")





        val recyclerview = findViewById<RecyclerView>(R.id.textView_details_services)


        if (chosenServices != null) {
            for(i in chosenServices){
                listOfSummaryServices.add(SummaryViewModel(i))
            }
        }


        recyclerview.adapter = adapter

        findViewById<TextView>(R.id.textView_summary_price).text = priceSum.toString()
        findViewById<TextView>(R.id.textView_summary_time).text = timeSum.toString()
        findViewById<TextView>(R.id.textView_summary_date).text = selectedDateTime.toString()





    }
}