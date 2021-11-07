package com.example.barberqueue

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.petcare.db.User
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.auth.AuthResult




class Registration : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        val editEmail = findViewById<EditText>(R.id.signup_email)
        val editPassword = findViewById<EditText>(R.id.signup_password)
        val editPassword2 = findViewById<EditText>(R.id.signup_password_rpt)
        val signUpButton= findViewById<Button>(R.id.sign_up_btn1)
        mAuth = FirebaseAuth.getInstance()


        signUpButton.setOnClickListener{
            if(editEmail.text.trim().toString().isNotEmpty() || editPassword.text.trim().toString().isNotEmpty()){

                if(dataValidation(editEmail.text.toString(),editPassword.text.toString(),editPassword2.text.toString())) {
                        createUser(editEmail.text.trim().toString(),editPassword.text.trim().toString())
                    }
            /*}else{
                Toast.makeText(this,"Wypełnij wszystkie pola",Toast.LENGTH_LONG).show()
            }*/
            }
        }
    }



    private fun createUser(email:String, password:String){




        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, this::handleRegistration)

    }

    private fun addUserToDb(email :String, uid: String){
        val db = FirebaseFirestore.getInstance()
        db.collection("Users").add(User(uid, email))
            .addOnCompleteListener(this, this::handleDbResult)
    }

    private fun handleDbResult(documentReferenceTask: Task<DocumentReference>) {
        if (documentReferenceTask.isSuccessful) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            mAuth.currentUser?.delete()
            Toast.makeText(this, "Error:" + documentReferenceTask.exception, Toast.LENGTH_SHORT)
                .show()
        }
    }
    private fun handleRegistration(authResultTask: Task<AuthResult>) {
        if (authResultTask.isSuccessful && mAuth.currentUser != null) {
            addUserToDb(mAuth.currentUser!!.email.toString(), mAuth.currentUser!!.uid.toString())
        } else {
            Toast.makeText(this, "Error:" + authResultTask.exception, Toast.LENGTH_SHORT).show()
        }
    }

    private fun  dataValidation(email: String, password: String, passwordRpt : String) : Boolean{
        if(email.isNotEmpty() && password.isNotEmpty() && passwordRpt.isNotEmpty()){
            if ( !email.contains("@")) {
                Toast.makeText(this, "Wrong email format", Toast.LENGTH_SHORT).show()
                return false
            }
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
            Toast.makeText(this, "No blank space allowed!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true

    }


}

