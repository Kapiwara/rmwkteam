package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePassword : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password)
        val submitPassword = findViewById<Button>(R.id.submit_pass_btn)
        auth = FirebaseAuth.getInstance()


        submitPassword.setOnClickListener {
             changePassword()

        }
    }

    private fun changePassword() {
        val oldPassword = findViewById<EditText>(R.id.old_pass)
        val newPassword = findViewById<EditText>(R.id.change_pass)
        val newPassword2 = findViewById<EditText>(R.id.change_pass_2)

        if(newPassword.text.isNotEmpty() && newPassword2.text.isNotEmpty() && oldPassword.text.isNotEmpty()){

            if(newPassword2.text.toString() == newPassword.text.toString()){
                val user = auth.currentUser

                if (user != null && user.email != null){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, oldPassword.text.toString() )

                    user.reauthenticate(credential)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                Toast.makeText(this, "Re-authentication success", Toast.LENGTH_SHORT).show()
                                user!!.updatePassword(newPassword.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                            openActivityMainActivity()
                                            finish()
                                        }
                                    }
                            }
                            else{

                                Toast.makeText(this, "Re-authentication failed", Toast.LENGTH_SHORT).show()
                            }
                        }

                }
                else{
                    openActivityMainActivity()
                }
            }
            else{
                Toast.makeText(this, "Password mismatching", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Fill blank spaces", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openActivityMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }
}