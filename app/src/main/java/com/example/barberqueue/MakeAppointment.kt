package com.example.barberqueue

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barberqueue.adapters.HoursAdapter
import com.example.barberqueue.databinding.ActivityMakeAppointmentBinding
import com.example.barberqueue.db.Settings
import com.example.barberqueue.interfaces.FromMakeAppointmentToSummary
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.beans.PropertyChangeSupport
import java.util.*


class MakeAppointment : AppCompatActivity(), FromMakeAppointmentToSummary {

    private lateinit var db: FirebaseFirestore
    private var listOfHours = ArrayList<HoursViewModel>()
    private lateinit var binding:ActivityMakeAppointmentBinding
    var adapter = HoursAdapter(listOfHours, this)
    private var selectedHour: String = "0:00"
    private lateinit var value: Settings
    val hoursList = listOf("8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00")


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        value = Settings()
        binding.selectedDateTextView.text = "          "

        val calendar: Calendar = Calendar.getInstance()
        // pobranie dotychczasowych danych o wizycie
        val priceSum = intent.getStringExtra("priceSum")
        val timeSum = intent.getStringExtra("timeSum")
        val chosenServices = intent.getStringArrayListExtra("chosenServices")
        var selectedDate: String = ""

        // przypisanie warto??ci do kalendarza
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val ref = db.collection("CalendarManagement").document("settings")
        val ref1 = FirebaseDatabase.getInstance().getReference("HoursStatus").child("Future")

        // pobranie z bazy danych o liczbie dni na kt??re mog?? si?? zapisywa?? u??ytkownicy
        ref.get().addOnSuccessListener {
            value = it.toObject(Settings::class.java)!!
        }

        // deklaracja kalendarza do wybierania dnia
        val datePickerDialog = DatePickerDialog(
            this,
            {_, _year, _month, _dayOfMonth ->
                //ci??g operacji kt??re wykonaj?? si?? po klikni??ciu OK na kalendarzu
                val dateString = _dayOfMonth.toString() + "." + (_month+1).toString() + "." + _year.toString()
                selectedDate = dateString
                binding.selectedDateTextView.text = selectedDate
                // wywo??anie funkcji aktualizuj??cej godziny na dany dzie??
                updateHours(_dayOfMonth.toString() + "." + (_month+1).toString() + "." + _year.toString())
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = calendar.time.time

        // wy??wietlenie kalendarza pod warunkiem ??e wszystkie dane zosta??y poprawnie pobrane z bazy
        ref.get().addOnSuccessListener {
            value = it.toObject(Settings::class.java)!!
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis + (value.further_days * 24 * 60 * 60 * 1000)
            datePickerDialog.show()
        }

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.hours_recycler_view)

        // this creates a vertical layout Manager
        val numberOfColumns = 4
        recyclerview.layoutManager = GridLayoutManager(this, numberOfColumns)
        // ArrayList of class ItemsViewModel

        // This loop will create 20 Views containing
        // the image with the count of view
        for(i in hoursList){
            listOfHours.add(HoursViewModel(i))
        }

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        PropertyChangeSupport(selectedDate).addPropertyChangeListener {

        }

        // przypisanie funkcji pod przyciski
        binding.currentData.setOnClickListener { datePickerDialog.show()}
        binding.goBackBtn.setOnClickListener { finish()}
        binding.selectDateBtn.setOnClickListener{

            if(selectedDate.isNotEmpty() and !(selectedHour.contentEquals("0:00"))) {

                // przekazanie danych o wizycie do podsumowania
                val intent = Intent(this@MakeAppointment, SummaryActivity::class.java)
                intent.putExtra("chosenServices", chosenServices)
                intent.putExtra("priceSum", priceSum)
                intent.putExtra("timeSum", timeSum)
                intent.putExtra("selectedDate", "$selectedDate")
                intent.putExtra("selectedHour", "$selectedHour")




                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Please choose correct date and time", Toast.LENGTH_SHORT).show()
            }
        }
    }


   override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun getSelectedTime(time: String) {
        selectedHour = time
    }

    // funkcja aktualizuj??ca godziny mo??liwe do wybrania po zmianie dnia
    fun updateHours(date: String){
        val ref = FirebaseDatabase.getInstance().getReference("HourStatus").child(date.replace('.','_'))
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfHours.clear()
                // sprawdzenie czy w bazie na dany dzie?? s?? ju?? zapisane jakie?? wizyty, je??li nie to aktualizujemy wszyskie godziny z tablicy hoursList
                if (dataSnapshot.exists()){
                    var a: Int = 0
                    for (i in dataSnapshot.children){
                        if (i.value == true) {
                            listOfHours.add(HoursViewModel(hoursList[a]))
                        }
                        a += 1
                    }

                }else{
                    val hoursList = listOf("8:00","8:30","9:00","9:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00")
                    for(i in hoursList){
                        listOfHours.add(HoursViewModel(i))
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addListenerForSingleValueEvent(postListener)
    }

}