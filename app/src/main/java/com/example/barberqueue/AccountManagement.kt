package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AccountManagement : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acc_management)
        val changePasswordButton = findViewById<Button>(R.id.change_pass_btn)
        changePasswordButton.setOnClickListener{ openActivityChangePassword() }
    }

    private fun openActivityChangePassword() {
        val intent = Intent(this, ChangePassword::class.java)
        startActivity(intent);
    }
}