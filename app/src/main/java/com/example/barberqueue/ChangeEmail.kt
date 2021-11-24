package com.example.barberqueue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.barberqueue.databinding.AccManagementBinding
import com.example.barberqueue.databinding.ActivityAdminLoginPanelBinding
import com.example.barberqueue.databinding.ActivityChangeEmailBinding
import com.example.barberqueue.databinding.ChangeEmailBinding
import com.example.barberqueue.db.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider.getCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangeEmail : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityChangeEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.emailSubmit.setOnClickListener{ editMail() }
    }

    private fun editMail() {

        if (binding.newEmail.getText().toString().equals(binding.newEmailRepeat.getText().toString())) {
           auth.signInWithEmailAndPassword(binding.currentEmail.getText().toString(), binding.emailVerPass.getText().toString())
               .addOnCompleteListener(this){ task ->
                   if(task.isSuccessful){
                       auth.currentUser!!.updateEmail(binding.newEmail.getText().toString())
                           .addOnCompleteListener{ task ->
                               if (task.isSuccessful){
                                   updateUserEmailToDb(binding.newEmail.getText().toString())
                                   Toast.makeText(getApplicationContext(),"Email updated", Toast.LENGTH_LONG).show();
                                   openActivityuserAcc();
                               }else{
                                   Toast.makeText(getApplicationContext(),"Wrong new E-mail format!", Toast.LENGTH_LONG).show();
                                   openActivityuserAcc();
                               }
                               }


                   }else{
                       Toast.makeText(getApplicationContext(),"Auth failed", Toast.LENGTH_LONG).show()
                       openActivityuserAcc()
                   }

               }}else{

                Toast.makeText(getApplicationContext(),"Passwords don't match, please try again.", Toast.LENGTH_LONG).show()

         }


    }


    private fun updateUserEmailToDb(email :String){
        val db = FirebaseFirestore.getInstance()
        val userid = auth.currentUser?.uid
        db.collection("Users").document(userid.toString()).update("login",email)
    }



    private fun openActivityuserAcc() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
    }


}