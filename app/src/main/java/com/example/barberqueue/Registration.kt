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
        mAuth = FirebaseAuth.getInstance()


        binding.signUpBtn1.setOnClickListener{

            if(dataValidation(binding.signupEmail.text.toString(),binding.signupPassword.text.toString(),binding.signupPasswordRpt.text.toString(),binding.signupName.text.toString(),binding.signupPhone.text.toString())) {
                createUser(binding.signupEmail.text.trim().toString(),binding.signupPassword.text.trim().toString(),binding.signupName.text.toString(),binding.signupPhone.text.toString())
            }

        }

        binding.logInBtn1.setOnClickListener { openActivityLogin()}

    }


    private fun openActivityLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun createUser(email:String, password:String, name: String, phone: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful && mAuth.currentUser != null) {
                    addUserToDb(mAuth.currentUser!!.email.toString(), name, phone)
                } else {
                    Toast.makeText(this, "Error:" + it.exception, Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addUserToDb(email :String, name: String, phone: String){
        val db = FirebaseFirestore.getInstance()
        val userid = mAuth.currentUser?.uid
        db.collection("Users").document(userid.toString()).set(User(email, name, phone))
            .addOnCompleteListener{
                handleDbResult(it)
            }
    }

    private fun handleDbResult(documentReferenceTask: Task<Void>) {
        if (documentReferenceTask.isSuccessful) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            mAuth.currentUser?.delete()
            Toast.makeText(this, "Error:" + documentReferenceTask.exception, Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun  dataValidation(email: String, password: String, passwordRpt : String, name: String, phone: String) : Boolean{
        if(email.isNotEmpty() && password.isNotEmpty() && passwordRpt.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()){
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
            if (phone.length > 12)
            {
                Toast.makeText(this, "Phone number must be at most 12 characters long", Toast.LENGTH_SHORT).show()
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

