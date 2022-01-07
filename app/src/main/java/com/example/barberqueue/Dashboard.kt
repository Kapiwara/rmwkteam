package com.example.barberqueue

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
    private var notificationId: Int = 0
    private lateinit var database: DatabaseReference
    private lateinit var orderArrayList: ArrayList<OrderForm>
    private lateinit var orderIdArrayList: ArrayList<String>
    private lateinit var auth: FirebaseAuth
    private var backPressedTime = 0L

    private lateinit var binding: DashboardBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.add_new_visit_btn)
        binding.addNewVisitBtn.setOnClickListener {
            openActivityNewVisit()
            finish()
        }

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
                    for (appointmentSnapshot in snapshot.children) {
                        val appointment = appointmentSnapshot.getValue(OrderForm::class.java)
                        if (appointment != null) {
                            if (appointment.userId == auth.currentUser?.uid && LocalDate.parse(appointment.date, formater) >= current) {
                                orderArrayList.add(appointment)
                                orderIdArrayList.add(appointmentSnapshot.key.toString())
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

        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val value = snapshot.getValue(OrderForm::class.java)
                if (value != null) {
                    Log.e("id", value.userId.toString())
                }
                if (value != null) {
                    if (value.userId == auth.currentUser?.uid){
                        createNotificationChangeStatusVisit()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }



    private fun openActivityContact() {
        val intent = Intent(this, ContactData::class.java)
        startActivity(intent)
    }

    private fun createNotificationChangeStatusVisit(){
        val name = "4hair_user_notify_change"
        val descriptionText = "4hair_user_notify_change"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        var builder = NotificationCompat.Builder(this, name)
            .setSmallIcon(R.drawable.ic_logo4hair)
            .setContentTitle("Status has changed")
            .setContentText("Check out appointments, because at least one of them has changed status!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText("Check out appointments, because at least one of them has changed status!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
            notificationId.inc()
        }
    }


    private fun openActivityAccountManagement() {
        val intent = Intent(this, AccountManagement::class.java)
        startActivity(intent)
    }


    private fun openActivityNewVisit() {
        val intent = Intent(this, NewVisit::class.java)
        startActivity(intent)
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


}





