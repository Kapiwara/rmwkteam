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



            if(dataValidation(newPassword.text.toString(),newPassword2.text.toString())){
                val user = auth.currentUser

                if (user != null && user.email != null){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, oldPassword.text.toString() )

                    user.reauthenticate(credential)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                Toast.makeText(this, "Re-authentication success", Toast.LENGTH_SHORT).show()
                                user.updatePassword(newPassword.text.toString())
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

                                Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                            }
                        }

                }
                else{
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    openActivityDashboard()
                }
            }

    }

    private fun openActivityMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun openActivityDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
    }

    private fun  dataValidation(password: String, passwordRpt : String) : Boolean{
        if(password.isNotEmpty() && passwordRpt.isNotEmpty()){

            if(password.length < 7) {
                Toast.makeText(this, "Password must be at least 7 characters long ", Toast.LENGTH_SHORT).show()
                return false
            }
            if(password != passwordRpt){
                Toast.makeText(this, "Passwords must be identical", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        else{
            Toast.makeText(this, "No blank spaces allowed!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }
}