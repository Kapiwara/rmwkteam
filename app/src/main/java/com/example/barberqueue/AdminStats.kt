package com.example.barberqueue

import android.graphics.Color
import android.graphics.Color.green
import android.graphics.Color.red
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.barberqueue.adapters.AppointmentsAdapter
import com.example.barberqueue.databinding.ActivityAdminPanelBinding
import com.example.barberqueue.databinding.ActivityAdminStatsBinding
import com.example.barberqueue.db.OrderForm


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AdminStats : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var orderArrayList: ArrayList<OrderForm>
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAdminStatsBinding
    var febCount=0;
    var janCount=0;
    var decCount=0;
    var novCount=0;
    var octCount=0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        getData()

    }


    private fun getData() {
        database = FirebaseDatabase.getInstance().getReference("FutureAppointment")
        database.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //Log.w("TAG", "app_added1")
                    for (appointmentSnapshot in snapshot.children) {
                        val appointment = appointmentSnapshot.getValue(OrderForm::class.java)
                        if (appointment != null) {
                            if(appointment.date.toString().substring(3,5) == "10" || appointment.date.toString().substring(2,4) == "10"){
                                octCount++;
                            }
                            else if(appointment.date.toString().substring(3,5) == "11" || appointment.date.toString().substring(2,4) == "11"){
                                novCount++;
                            }
                            else if(appointment.date.toString().substring(2,4) == "12" || appointment.date.toString().substring(3,5) == "12"){
                                decCount++;
                            }
                            else if(appointment.date.toString().substring(2,3) == "1" || appointment.date.toString().substring(3,4) == "1" ){
                                janCount++;
                            }
                            else if(appointment.date.toString().substring(2,3) == "2" || appointment.date.toString().substring(3,4) == "2" ){
                                febCount++;
                            }

                            Log.w("january", janCount.toString())
                            Log.w("nov", novCount.toString())
                            Log.w("oct", octCount.toString())
                            Log.w("december", decCount.toString())
                            Log.w("feb", febCount.toString())

                            val xvalue=ArrayList<String>()
                            xvalue.add("OCT.")
                            xvalue.add("NOV.")
                            xvalue.add("DEC.")
                            xvalue.add("JAN.")
                            xvalue.add("FEB.")

                            // here we need to modify data
                            val lineentry=ArrayList<Entry>();
                            lineentry.add(Entry(octCount.toFloat(),0)) // month:09
                            lineentry.add(Entry(novCount.toFloat(),1)) // month:10
                            lineentry.add(Entry(decCount.toFloat(),2)) //month:11
                            lineentry.add(Entry(janCount.toFloat(),3)) //month:12
                            lineentry.add(Entry(febCount.toFloat(),4)) //month:01


                            val linedataset = LineDataSet(lineentry, "Number of visits")
                            linedataset.color=resources.getColor(R.color.beige)

                            val data = LineData(xvalue,linedataset)
                            binding.lineChart.data = data;
                            binding.lineChart.setBackgroundColor(resources.getColor(R.color.white))
                            binding.lineChart.animateXY(3000,3000)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled")
            }

        })
    }

}