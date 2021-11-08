package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.AccManagementBinding

class AccountManagement : AppCompatActivity(){
    private lateinit var binding: AccManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AccManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val changePasswordButton = findViewById<Button>(R.id.change_pass_btn)
        binding.changePassBtn.setOnClickListener{ openActivityChangePassword() }
    }

    private fun openActivityChangePassword() {
        val intent = Intent(this, ChangePassword::class.java)
        startActivity(intent)
    }
}