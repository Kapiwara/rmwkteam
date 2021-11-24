package com.example.barberqueue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.barberqueue.databinding.DashboardBinding
import com.example.barberqueue.db.Service
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DialogEditProfile(val name:String, private val phone:String): BottomSheetDialogFragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog)
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_edit_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextName = view.findViewById<EditText>(R.id.editText_edit_profile_name)
        val editTextPhone = view.findViewById<EditText>(R.id.editText_edit_profile_phone)
        val saveProfileDataBtn = view.findViewById<ConstraintLayout>(R.id.save_profile_data_btn)

        editTextName.setText(name)
        editTextPhone.setText(phone)

        saveProfileDataBtn.setOnClickListener {
            updateUserEmailToDb(editTextName.text.toString(), editTextPhone.text.toString())
        }

    }

    private fun updateUserEmailToDb(name :String, phone: String){
        if (name.isNotEmpty() && phone.isNotEmpty() && phone.length < 12){
            val db = FirebaseFirestore.getInstance()
            val userid = auth.currentUser?.uid
            db.collection("Users").document(userid.toString()).update( mapOf("name" to name, "phone" to phone)).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context,"Profil updated", Toast.LENGTH_LONG).show()
                    dismiss()
                }
                else{
                    Toast.makeText(context,"Wrong data!", Toast.LENGTH_LONG).show()
                }
            }
        }
        else{
            Toast.makeText(context,"Wrong data!", Toast.LENGTH_LONG).show()
        }

    }
}



