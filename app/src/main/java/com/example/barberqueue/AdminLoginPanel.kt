package com.example.barberqueue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class AdminLoginPanel : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login_panel)
        auth = FirebaseAuth.getInstance()
        val loginButton = findViewById<Button>(R.id.log_in_mgmt_btn)
        val loginEmail = findViewById<EditText>(R.id.login_email_mgmt)
        val loginPassword = findViewById<EditText>(R.id.login_password_mgmt)

        loginButton.setOnClickListener {
            if(loginEmail.text.trim().toString().isNotEmpty() || loginPassword.text.trim().toString().isNotEmpty()){
                loginAdmin(loginEmail.text.trim().toString(),loginPassword.text.trim().toString())
            }
            else{
                Toast.makeText(this,"No blank spaces allowed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginAdmin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                    //credentials:
                    // admin@gmail.com
                    //1234567
                    if(currentFirebaseUser!!.uid == "t6YSIazMwScSSW4dVgLZgBHiDgH3"){
                        Toast.makeText(baseContext, "Hello Admin", Toast.LENGTH_SHORT).show()
                        openActivityPanel()
                    }else {
                        Toast.makeText(baseContext, "You are trying to log in as a user, please use a differ" +
                                "ent panel for it", Toast.LENGTH_LONG).show()
                        Firebase.auth.signOut()
                        finish()
                        openActivityUserLogin()
                    }

                } else {
                    Toast.makeText(baseContext, "Authentication failed. Wrong credentials",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun openActivityPanel() {

        val intent = Intent(this, AdminPanel::class.java)
        startActivity(intent)
    }
    private fun openActivityUserLogin() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}