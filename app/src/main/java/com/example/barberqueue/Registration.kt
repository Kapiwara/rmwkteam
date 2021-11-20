package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.RegistrationBinding
import com.example.barberqueue.db.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class Registration : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: RegistrationBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = RegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val editEmail = findViewById<EditText>(R.id.signup_email)
        //val editPassword = findViewById<EditText>(R.id.signup_password)
        //val editPassword2 = findViewById<EditText>(R.id.signup_password_rpt)
        //val signUpButton= findViewById<Button>(R.id.sign_up_btn1)
        mAuth = FirebaseAuth.getInstance()


        binding.signUpBtn1.setOnClickListener{

            if(dataValidation(binding.signupEmail.text.toString(),binding.signupPassword.text.toString(),binding.signupPasswordRpt.text.toString())) {
                createUser(binding.signupEmail.text.trim().toString(),binding.signupPassword.text.trim().toString())
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
            addUserToDb(mAuth.currentUser!!.email.toString(), mAuth.currentUser!!.uid)
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

