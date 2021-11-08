package com.example.barberqueue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
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
        val mgmtBtn = findViewById<Button>(R.id.mgmt_btn)
        auth = FirebaseAuth.getInstance()
        signupButton.setOnClickListener { openActivityRegistration() }
        mgmtBtn.setOnClickListener { openActivityManage() }
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
                    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser

                    if(currentFirebaseUser!!.uid == "t6YSIazMwScSSW4dVgLZgBHiDgH3"){
                        Toast.makeText(baseContext, "To log in as an admin please use a different panel", Toast.LENGTH_LONG).show()
                        Firebase.auth.signOut()
                        finish()
                        openActivityManage()
                    }else {
                        Toast.makeText(baseContext, "Authentication succeed", Toast.LENGTH_LONG).show()
                        openActivityUser()
                    }
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
            if(currentUser.uid == "t6YSIazMwScSSW4dVgLZgBHiDgH3"){
                openActivityAdminPanel()
            }else{
                openActivityUser()
            }

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
    private fun openActivityManage() {

        val intent = Intent(this, AdminLoginPanel::class.java)
        startActivity(intent)
    }
    private fun openActivityAdminPanel() {

        val intent = Intent(this, AdminPanel::class.java)
        startActivity(intent)
    }


}