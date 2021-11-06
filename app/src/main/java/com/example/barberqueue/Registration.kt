package com.example.barberqueue

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class Registration : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        val editEmail = findViewById<EditText>(R.id.signup_email)
        val editPassword = findViewById<EditText>(R.id.signup_password)
        val signUpButton= findViewById<Button>(R.id.sign_up_btn1)
        auth = FirebaseAuth.getInstance();


        signUpButton.setOnClickListener{
            if(editEmail.text.trim().toString().isNotEmpty() || editPassword.text.trim().toString().isNotEmpty()){

                createUser(editEmail.text.trim().toString(),editPassword.text.trim().toString())
            }else{
                Toast.makeText(this,"Wypełnij wszystkie pola",Toast.LENGTH_LONG).show()
            }
        }
    }



    fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("task msg", "createUserWithEmail:success")
                    val user = auth.currentUser
                    var intent = Intent(this,MainActivity::class.java);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("task msg", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }



}
