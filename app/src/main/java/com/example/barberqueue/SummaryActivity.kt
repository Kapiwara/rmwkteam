package com.example.barberqueue

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.adapters.SummaryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

private var listOfSummaryServices = ArrayList<SummaryViewModel>()
var adapter = SummaryAdapter(listOfSummaryServices)

class SummaryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var newAppointment: OrderForm

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        auth = FirebaseAuth.getInstance()


        val priceSum = intent.getStringExtra("priceSum")?.toFloat()
        val timeSum = intent.getStringExtra("timeSum")?.toInt()
        val chosenServices = intent.getStringArrayExtra("chosenServices")
        val selectedDate = intent.getStringExtra("selectedDate")
        val selectedHour = intent.getStringExtra("selectedHour")




        val recyclerview = findViewById<RecyclerView>(R.id.textView_details_services)


        if (chosenServices != null) {
            for(i in chosenServices){
                listOfSummaryServices.add(SummaryViewModel(i))
                Log.e("Item", i.toString())
            }
        }


        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        findViewById<TextView>(R.id.textView_summary_price).text = priceSum.toString() + " zł"
        findViewById<TextView>(R.id.textView_summary_time).text = timeSum.toString() + " min"
        findViewById<TextView>(R.id.textView_summary_date).text = "$selectedDate   $selectedHour"

        findViewById<Button>(R.id.book_appointment_btn).setOnClickListener {
            if (priceSum != null && timeSum != null){
                newAppointment = OrderForm(selectedDate,selectedHour,false,false,false, priceSum, listOfSummaryServices, timeSum, auth.currentUser?.uid)

                val ref = FirebaseDatabase.getInstance().getReference("FutureAppointment")
                val newRef = ref.push()
                val key = newRef.key

                if (key != null) {
                    ref.child(key).setValue(newAppointment).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Booking successful.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this,Dashboard::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "Booking failed.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else{
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show()
            }
        }
    }
}