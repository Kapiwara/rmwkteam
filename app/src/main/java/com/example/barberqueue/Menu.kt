package com.example.barberqueue

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityMenuBinding

class Menu: AppCompatActivity() {

    //private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}