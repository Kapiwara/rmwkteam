package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.databinding.AccManagementBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.content.DialogInterface

import android.widget.Toast

import com.google.android.gms.tasks.Task

import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.example.barberqueue.db.User

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore


class AccountManagement : AppCompatActivity(){
    private lateinit var binding: AccManagementBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    lateinit var obj: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AccManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // przypisanie odpowiednich funkcji pod przyciski
        binding.changePassBtn.setOnClickListener{ openActivityChangePassword() }
        binding.changeEmailBtn.setOnClickListener{ openActivityChangeEmail() }
        binding.deleteAccBtn.setOnClickListener { deleteUser() }
        binding.editProfileDataBtn.setOnClickListener{ openDialogChangeProfileData() }


        // pobranie z bazy danych informacji o profilu użytkownika
        db = FirebaseFirestore.getInstance()
        val ref = auth.currentUser?.let { db.collection("Users").document(it.uid) }

        // wstawienie danych o profilu użytkownika w widoki
        ref?.addSnapshotListener { value, error ->
            obj = value?.toObject(User::class.java)!!
            binding.textViewName.text = obj.name
            binding.textViewPhone.text = obj.phone
        }
    }

    // funkcja usuwająca konto użytkownika
    private fun deleteUser() {
        val user = FirebaseAuth.getInstance().currentUser
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Are you sure?")
        dialog.setMessage("Deleting your account permanently will result in all the data loss without a possibility of getting it back.")
        dialog.setPositiveButton("Delete",
            DialogInterface.OnClickListener { dialog, i ->
                user?.delete()?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Account deleted", Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@AccountManagement, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@AccountManagement,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            })
        dialog.setNegativeButton("Dissmiss",
            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })

        val alertDialog: AlertDialog = dialog.create()
        alertDialog.show()
    }

    private fun openActivityChangePassword() {
        val intent = Intent(this, ChangePassword::class.java)
        startActivity(intent)
    }

    private fun openActivityChangeEmail() {
        val intent = Intent(this, ChangeEmail::class.java)
        startActivity(intent)
    }

    private fun openDialogChangeProfileData() {
        val bottomSheetFragment = DialogEditProfile(obj?.name.toString(), obj?.phone.toString())
        bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
    }

}