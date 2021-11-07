package com.example.barberqueue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val signupButton = findViewById<Button>(R.id.sign_up_btn)
        val loginButton = findViewById<Button>(R.id.log_in_btn)
        val loginEmail = findViewById<EditText>(R.id.login_email)
        val loginPassword = findViewById<EditText>(R.id.login_password)
        auth = FirebaseAuth.getInstance()
        signupButton.setOnClickListener { openActivityRegistration() }
        loginButton.setOnClickListener {
            if(loginEmail.text.trim().toString().isNotEmpty() || loginPassword.text.trim().toString().isNotEmpty()){
                loginUser(loginEmail.text.trim().toString(),loginPassword.text.trim().toString())
            }
            else{
                Toast.makeText(this,"No blank spaces allowed",Toast.LENGTH_LONG).show()
            }
        }



    }

    private fun loginUser(email:String, password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Authentication succeeded.",
                        Toast.LENGTH_SHORT).show()
                    openActivityUser()
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            openActivityUser()
        }
    }



    private fun openActivityRegistration() {
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
    }

    private fun openActivityUser() {

        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
    }


}