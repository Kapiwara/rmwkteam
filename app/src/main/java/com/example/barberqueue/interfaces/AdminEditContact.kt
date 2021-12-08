package com.example.barberqueue.interfaces

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.ActivityAdminEditContactBinding
import com.example.barberqueue.db.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AdminEditContact : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEditContactBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    lateinit var obj: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance();

        val ref =db.collection("Contact").document("tbuXFCZEPmiHIpdtCpHD")
        ref?.addSnapshotListener { value, error ->
            obj = value?.toObject(Contact::class.java)!!
            val editable_about_us: Editable = SpannableStringBuilder(obj.about_us)
            val editable_adress: Editable = SpannableStringBuilder(obj.contact_adress)
            val editable_phone: Editable = SpannableStringBuilder(obj.contact_phone)
            binding.aboutUsEdit.text=editable_about_us
            binding.adressEdit.text=editable_adress
            binding.phoneEdit.text=editable_phone
        }

        binding.editContactBtn.setOnClickListener{
            updateContact(binding.aboutUsEdit.editableText, binding.adressEdit.editableText, binding.phoneEdit.editableText)
        }
    }

    private fun updateContact(abs: Editable?, adress: Editable?, phone: Editable?) {
        println(abs)
        println(adress)
        println(phone)
        if(!abs.isNullOrEmpty() && !adress.isNullOrEmpty() && !phone.isNullOrEmpty()){
            val db = FirebaseFirestore.getInstance()
            db.collection("Contact").document("tbuXFCZEPmiHIpdtCpHD")
                .update( mapOf("about_us" to abs.toString(), "contact_adress" to adress.toString(), "contact_phone" to phone.toString()))
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        Toast.makeText(this,"Contact data updated!", Toast.LENGTH_LONG).show()

                    }
                    else{
                        Toast.makeText(this,"Something went wrong.", Toast.LENGTH_LONG).show()
                    }
                }
        }else{
            Toast.makeText(this,"No blank spaces", Toast.LENGTH_LONG).show()
        }
    }
}