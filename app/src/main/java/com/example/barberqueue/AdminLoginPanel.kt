package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityAdminLoginPanelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AdminLoginPanel : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAdminLoginPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        binding.logInMgmtBtn.setOnClickListener {
            if(binding.loginEmailMgmt.text.trim().toString().isNotEmpty() || binding.loginPasswordMgmt.text.trim().toString().isNotEmpty()){
                loginAdmin(binding.loginEmailMgmt.text.trim().toString(),binding.loginPasswordMgmt.text.trim().toString())
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
                    if(currentFirebaseUser!!.uid == "FYjsiIq28NMw9x8AENvygDBN1TC2"){
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