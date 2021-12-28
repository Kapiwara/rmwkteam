package com.example.barberqueue

import android.graphics.Color
import android.graphics.Color.green
import android.graphics.Color.red
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.barberqueue.databinding.ActivityAdminPanelBinding
import com.example.barberqueue.databinding.ActivityAdminStatsBinding

import java.util.ArrayList


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate;






class AdminStats : AppCompatActivity() {
    private lateinit var binding: ActivityAdminStatsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    setLineChartData()

    }
    fun setLineChartData(){
    val xvalue=ArrayList<String>()
        xvalue.add("SEP.")
        xvalue.add("OCT.")
        xvalue.add("NOV.")
        xvalue.add("DEC.")
        xvalue.add("JAN.")

// here we need to modify data
        val lineentry=ArrayList<Entry>();
        lineentry.add(Entry(20f,0)) // month:09
        lineentry.add(Entry(13f,1)) // month:10
        lineentry.add(Entry(49f,2)) //month:11
        lineentry.add(Entry(34f,3)) //month:12
        lineentry.add(Entry(31f,4)) //month:01


        val linedataset = LineDataSet(lineentry, "Number of visits")
        linedataset.color=resources.getColor(R.color.beige)

        val data = LineData(xvalue,linedataset)
        binding.lineChart.data = data;
        binding.lineChart.setBackgroundColor(resources.getColor(R.color.white))
        binding.lineChart.animateXY(3000,3000)
    }

}