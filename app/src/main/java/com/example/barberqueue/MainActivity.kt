package com.example.barberqueue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


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
                Toast.makeText(this,"Wypełnij wszystkie pola",Toast.LENGTH_LONG).show()
            }
        }



    }

    private fun loginUser(email:String, password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("task", "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Authentication succeeded.",
                        Toast.LENGTH_SHORT).show()
                    openActivityUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("task", "signInWithEmail:failure", task.exception)
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

        val intent = Intent(this, UserAccount::class.java)
        startActivity(intent)
    }


}