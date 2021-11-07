package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Dashboard : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        val accMngBtn = findViewById<Button>(R.id.acc_mng_btn)
        val logoutBtn = findViewById<Button>(R.id.logout_btn)

        accMngBtn.setOnClickListener{ openActivityAccountManagement() }
        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            finish()
        }

    }



    private fun openActivityAccountManagement() {
        val intent = Intent(this, AccountManagement::class.java)
        startActivity(intent)
    }



}
