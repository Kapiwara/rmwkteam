package com.example.barberqueue

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.barberqueue.adapters.AdminOrderAdapter
import com.example.barberqueue.adapters.AppointmentsAdapter
import com.example.barberqueue.databinding.ActivityCalendarManagementBinding
import com.example.barberqueue.db.OrderForm
import com.example.barberqueue.db.Settings
import com.example.barberqueue.interfaces.AdminOrderClickView
import com.example.barberqueue.interfaces.OrderClickView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarManagementActivity : AppCompatActivity(), AdminOrderClickView{

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityCalendarManagementBinding
    private lateinit var orderArrayList: ArrayList<OrderForm>
    private lateinit var orderIdArrayList: ArrayList<String>
    private lateinit var database: DatabaseReference
    private var notificationId: Int = 0

    private lateinit var value: Settings

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)



        value = Settings()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val ref = db.collection("CalendarManagement").document("settings")

        binding.textViewFurtherDays.text = "-"

        ref.get().addOnSuccessListener {
            value = it.toObject(Settings::class.java)!!
            binding.textViewFurtherDays.text = value.further_days.toString()
        }

        binding.imageViewPrev.setOnClickListener {
            if (value.further_days > 1) {
                value.further_days = value.further_days.dec()
                refreshData(value)
            } else {
                Toast.makeText(this, "Can't set up lower value", Toast.LENGTH_LONG)
            }
        }

        binding.imageViewNext.setOnClickListener {
            if (value.further_days < 365) {
                value.further_days = value.further_days.inc()
                refreshData(value)
            } else {
                Toast.makeText(this, "Can't set up higher value", Toast.LENGTH_LONG)
            }
        }

        binding.saveChangesBtn.setOnClickListener {
            saveChangesInDatabase(value)
        }
        binding.adminAppointmentsView.layoutManager = LinearLayoutManager(this)
        binding.adminAppointmentsView.setHasFixedSize(true)
        orderArrayList = arrayListOf<OrderForm>()
        orderIdArrayList = arrayListOf<String>()

        getData()
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
                        if (appointment != null && LocalDate.parse(appointment.date, formater) >= current) {
                            if (true) {
                                orderArrayList.add(appointment)
                                orderIdArrayList.add(appointmentSnapshot.key.toString())
                                //Log.w("TAG", "app_added")
                            }
                        }

                    }

                    binding.adminAppointmentsView.adapter =
                        AdminOrderAdapter(orderArrayList, this@CalendarManagementActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled")
            }

        })

        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                createNotificationNewVisit()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                createNotificationChangeStatusVisit()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun refreshData(value: Settings) {
        binding.textViewFurtherDays.text = value.further_days.toString()
    }

    private fun saveChangesInDatabase(value: Settings) {
        db.collection("CalendarManagement").document("settings")
            .update(mapOf("further_days" to value.further_days))
    }

    private fun createNotificationNewVisit(){
        val name = "4hair_admin_notify_add"
        val descriptionText = "4hair_admin_notify_add"
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
            .setContentTitle("New visit")
            .setContentText("You have new visit to confirm!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("You have new visit to confirm!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
            notificationId.inc()
        }
    }

    private fun createNotificationChangeStatusVisit(){
        val name = "4hair_admin_notify_change"
        val descriptionText = "4hair_admin_notify_change"
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
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Check out appointments, because at least one of them has changed status!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
            notificationId.inc()
        }
    }

    override fun onClickOrder(orderForm: OrderForm, position: Int) {
        val intent = Intent(this, ViewAppointmentAdmin::class.java)
        intent.putExtra("order", orderForm)
        intent.putExtra("id", orderIdArrayList[position])
        startActivity(intent)
    }
}