package com.example.barberqueue

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.adapters.SummaryAdapter
import com.example.barberqueue.db.OrderForm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.math.ceil

private var listOfSummaryServices = ArrayList<SummaryViewModel>()

var adapter = SummaryAdapter(listOfSummaryServices)

class SummaryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var newAppointment: OrderForm
    var selectedHourId: Int = 0
    var additionalHoursAmount: Int = 0
    val hoursList = listOf("8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00")
    var hoursBooleanList = listOf(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        auth = FirebaseAuth.getInstance()

        // pobranie wszystkich danych o wizycie z poprzednich okien
        val priceSum = intent.getStringExtra("priceSum")?.toFloat()
        val timeSum = intent.getStringExtra("timeSum")?.toInt()
        val chosenServices = intent.getStringArrayListExtra("chosenServices")
        val selectedDate = intent.getStringExtra("selectedDate")
        val selectedHour = intent.getStringExtra("selectedHour")

        //sprawdzenie jaki indeks w tablicy ma wybrana godzina
        for ((id, value) in hoursList.withIndex()){
            if (value == selectedHour){
                selectedHourId = id
            }
        }

        // sprawdzenie ile nast??pnych godzin nale??y wykre??li?? z rezerwowania gdy wizyta jest d??u??sza ni?? 30 min
        if (timeSum != null) {
            if (timeSum > 30){
                additionalHoursAmount = ceil(timeSum.div(30).toFloat()).toInt()
            }
        }

        val recyclerview = findViewById<RecyclerView>(R.id.textView_details_services)

        // wstawienie us??ug do odpowiedniej tablicy
        if (chosenServices != null) {
            listOfSummaryServices.clear()
            for(i in chosenServices){
                listOfSummaryServices.add(SummaryViewModel(i))
            }
        }

        // ustwawienie adaptera do wy??wietalnia us??ug w kolumnie
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        // wstawienie w widoki informacji o wizycie
        findViewById<TextView>(R.id.textView_summary_price).text = priceSum.toString() + " z??"
        findViewById<TextView>(R.id.textView_summary_time).text = timeSum.toString() + " min"
        findViewById<TextView>(R.id.textView_summary_date).text = "$selectedDate   $selectedHour"

        // ci??g wykowywany po klikni??ciu guzika odpowiedzialnego za ostateczn?? rezerwacj?? wizyty
        findViewById<Button>(R.id.book_appointment_btn).setOnClickListener {
            if (priceSum != null && timeSum != null){
                //zebranie wszystkich informacji o wizycie w jeden obiekt
                newAppointment = OrderForm(selectedDate,selectedHour,false,false,false, priceSum, listOfSummaryServices, timeSum, auth.currentUser?.uid)

                // sprawdzenie czy dany dzie?? ma swoj?? instancj?? w bazie danych, je??li nie to tak?? tworzymy
                FirebaseDatabase.getInstance().getReference("HourStatus").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.child(newAppointment.date.toString().replace('.','_')).exists()){
                            FirebaseDatabase.getInstance().getReference("HourStatus").child(newAppointment.date.toString().replace('.','_')).setValue(hoursBooleanList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })

                val ref = FirebaseDatabase.getInstance().getReference("FutureAppointment")
                // utworzenie unikatowego klucza dla ka??dej wizyty
                val newRef = ref.push()
                val key = newRef.key

                if (key != null) {
                    // wstawienie wizyty do bazy danych
                    ref.child(key).setValue(newAppointment).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Booking successful.", Toast.LENGTH_LONG).show()

                            // okznaczenie zarezerwowanych godzin jako zaj??te
                            FirebaseDatabase.getInstance().getReference("HourStatus").child(newAppointment.date.toString().replace('.','_')).child(
                                selectedHourId.toString()
                            ).setValue(false)
                            if (additionalHoursAmount > 0){
                                for (i in 1 until  additionalHoursAmount){
                                    if (selectedHourId+i < 21){
                                        FirebaseDatabase.getInstance().getReference("HourStatus").child(newAppointment.date.toString().replace('.','_')).child(
                                            (selectedHourId+i).toString()
                                        ).setValue(false)
                                    }
                                }
                            }

                            finish()


                        } else {
                            Toast.makeText(this, "Booking failed.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            else{
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show()
            }

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
