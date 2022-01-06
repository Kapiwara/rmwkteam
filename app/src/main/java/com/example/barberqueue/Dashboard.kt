package com.example.barberqueue

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberqueue.adapters.AppointmentsAdapter
import com.example.barberqueue.databinding.DashboardBinding
import com.example.barberqueue.db.OrderForm
import com.example.barberqueue.interfaces.OrderClickView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Dashboard : AppCompatActivity(), OrderClickView {
    private var x1: Float = 0F
    private var y1: Float = 0F
    private var x2: Float = 0F
    private var y2: Float = 0F
    private lateinit var database: DatabaseReference
    private lateinit var orderArrayList: ArrayList<OrderForm>
    private lateinit var orderIdArrayList: ArrayList<String>
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: DashboardBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.add_new_visit_btn)
        binding.addNewVisitBtn.setOnClickListener { openActivityNewVisit() }

        binding.accMngBtn.setOnClickListener { openActivityAccountManagement() }
        binding.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }
        binding.logo.setOnClickListener {
            openActivityContact()
        }


        binding.appointmentsView.layoutManager = LinearLayoutManager(this)
        binding.appointmentsView.setHasFixedSize(true)

        orderArrayList = arrayListOf<OrderForm>()
        orderIdArrayList = arrayListOf<String>()

        getData()


    }

    override fun onClickOrder(order: OrderForm, position: Int) {

        val intent = Intent(this, ViewAppointment::class.java)
        intent.putExtra("order", order)
        intent.putExtra("id", orderIdArrayList[position])
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData() {

        val current = LocalDate.now()
        val formater = DateTimeFormatter.ofPattern("d.M.yyyy")

        database = FirebaseDatabase.getInstance().getReference("FutureAppointment")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderArrayList.clear()
                orderIdArrayList.clear()
                if (snapshot.exists()) {
                    //Log.w("TAG", "app_added1")
                    for (appointmentSnapshot in snapshot.children) {
                        val appointment = appointmentSnapshot.getValue(OrderForm::class.java)
                        if (appointment != null) {
                            if (appointment.userId == auth.currentUser?.uid && LocalDate.parse(appointment.date, formater) >= current) {
                                orderArrayList.add(appointment)
                                orderIdArrayList.add(appointmentSnapshot.key.toString())
                                //Log.w("TAG", "app_added")
                            }
                        }
                    }

                    binding.appointmentsView.adapter =
                        AppointmentsAdapter(orderArrayList, this@Dashboard)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled")
            }
        })
    }

    private fun openActivityMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun openActivityContact() {
        val intent = Intent(this, ContactData::class.java)
        startActivity(intent)
    }


    private fun changingTabs(position: Int) {

        if (position == 0) {

        }
        if (position == 1) {

        }
    }

    //funkcja do poruszania sie po ui w poziomie
    /*override fun onTouchEvent(touchEvent: MotionEvent): Boolean {

        when (touchEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = touchEvent.x
                y1 = touchEvent.y

            }

            MotionEvent.ACTION_UP -> {
                x2 = touchEvent.x
                y2 = touchEvent.y
                if (x1 < x2 && y1 <= y2 + 100 && y1 >= y2 - 100) {
                    //openActivityMenu()
                    Log.e("position", "$x1,$y1     $x2,$y2")
                } else if (x1 > x2 && y1 <= y2 + 100 && y1 >= y2 - 100) {
                    //openActivitySTH()
                    Log.e("position", "$x1,$y1     $x2,$y2")
                }


            }
        }


        return false
    }*/


    private fun openActivityAccountManagement() {
        val intent = Intent(this, AccountManagement::class.java)
        startActivity(intent)
    }


    private fun openActivityNewVisit() {
        val intent = Intent(this, NewVisit::class.java)
        startActivity(intent)
    }


}





