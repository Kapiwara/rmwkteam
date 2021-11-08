package com.example.barberqueue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityRightBinding


class Right: AppCompatActivity() {

    //private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRightBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityRightBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}