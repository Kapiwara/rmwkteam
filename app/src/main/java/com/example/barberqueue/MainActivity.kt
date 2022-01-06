package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.signUpBtn.setOnClickListener { openActivityRegistration() }
        binding.mgmtBtn.setOnClickListener { openActivityManage() }
        binding.logInBtn.setOnClickListener {
            if(binding.loginEmail.text.trim().toString().isNotEmpty() || binding.loginPassword.text.trim().toString().isNotEmpty()){
                loginUser(binding.loginEmail.text.trim().toString(),binding.loginPassword.text.trim().toString())
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

                    if(currentFirebaseUser!!.uid == "FYjsiIq28NMw9x8AENvygDBN1TC2"){
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
            if(currentUser.uid == "FYjsiIq28NMw9x8AENvygDBN1TC2"){
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