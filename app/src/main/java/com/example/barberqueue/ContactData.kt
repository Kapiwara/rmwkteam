package com.example.barberqueue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.barberqueue.databinding.AccManagementBinding
import com.example.barberqueue.databinding.ActivityContactDataBinding
import com.example.barberqueue.db.Contact
import com.example.barberqueue.db.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ContactData : AppCompatActivity() {
    private lateinit var binding: ActivityContactDataBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    lateinit var obj: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance();
        val ref =db.collection("Contact").document("tbuXFCZEPmiHIpdtCpHD")
        ref?.addSnapshotListener { value, error ->
            obj = value?.toObject(Contact::class.java)!!
            binding.aboutUsContact.text=obj.about_us
            binding.adressContact.text=obj.contact_adress
            binding.phoneContact.text=obj.contact_phone
        }
    }
}



