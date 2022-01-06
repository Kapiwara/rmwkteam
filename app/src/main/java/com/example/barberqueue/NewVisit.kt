package com.example.barberqueue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barberqueue.db.Service
import com.google.firebase.firestore.FirebaseFirestore


class NewVisit : AppCompatActivity() {


    private lateinit var db: FirebaseFirestore
    lateinit var women: CheckBox
    lateinit var men: CheckBox
    lateinit var coloringHaircut: CheckBox
    lateinit var coloringColors: CheckBox
    lateinit var decolorization: CheckBox
    lateinit var hairTreatment: CheckBox
    lateinit var hairWash: CheckBox
    lateinit var fringe: CheckBox
    lateinit var beard: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_visit)

        val servicesList = mutableListOf<Service>()
        db = FirebaseFirestore.getInstance()

        val womenPrice = findViewById<TextView>(R.id.textView_women_haircut_price)
        val menPrice = findViewById<TextView>(R.id.textView_men_haircut_price)
        val coloringPrice = findViewById<TextView>(R.id.textView_coloring_and_haircut_price)
        val coloringColorsPrice =
            findViewById<TextView>(R.id.textView_coloring_more_than_1_color_price)
        val decolorizationPrice = findViewById<TextView>(R.id.textView_decolorization_price)
        val hairTreatmentPrice = findViewById<TextView>(R.id.textView_hair_care_treatments_price)
        val hairWashPrice = findViewById<TextView>(R.id.textView_washing_and_hair_styling_price)
        val fringePrice = findViewById<TextView>(R.id.textView_fringe_trimming_price)
        val beardPrice = findViewById<TextView>(R.id.textView_beard_trimming_price)

        val women = findViewById<CheckBox>(R.id.checkBox_women_haircut)
        val men = findViewById<CheckBox>(R.id.checkBox_men_haircut)
        val coloring = findViewById<CheckBox>(R.id.checkBox_coloring_and_haircut)
        val coloringColors = findViewById<CheckBox>(R.id.checkBox_coloring_more_than_1_color)
        val decolorization = findViewById<CheckBox>(R.id.checkBox_decolorization)
        val hairTreatment = findViewById<CheckBox>(R.id.checkBox_hair_care_treatments)
        val hairWash = findViewById<CheckBox>(R.id.checkBox_washing_and_hair_styling)
        val fringe = findViewById<CheckBox>(R.id.checkBox_fringe_trimming)
        val beard = findViewById<CheckBox>(R.id.checkBox_beard_trimming)

        val chooseDate: Button = findViewById<Button>(R.id.choose_date_btn)
        var priceSum: Float = 0F
        var timeSum: Int = 0
        var chosenServices = arrayOf<String>()

        val ref = db.collection("Services")
        ref.get().addOnSuccessListener { services ->

            if (services != null) {

                for (document in services) {
                    try {
                        servicesList.add(document.toObject(Service::class.java))
                        Log.e("lista", (document.toObject(Service::class.java).toString()))
                    } catch (error: Exception) {
                        Toast.makeText(
                            this,
                            "wystąpił błąd podczas pobierania danych",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                women.text = servicesList[0].name.toString()
                men.text = servicesList[1].name.toString()
                coloring.text = servicesList[2].name.toString()
                coloringColors.text = servicesList[3].name.toString()
                decolorization.text = servicesList[4].name.toString()
                hairTreatment.text = servicesList[5].name.toString()
                hairWash.text = servicesList[6].name.toString()
                fringe.text = servicesList[7].name.toString()
                beard.text = servicesList[8].name.toString()

                womenPrice.text = servicesList[0].price.toString()
                menPrice.text = servicesList[1].price.toString()
                coloringPrice.text = servicesList[2].price.toString()
                coloringColorsPrice.text = servicesList[3].price.toString()
                decolorizationPrice.text = servicesList[4].price.toString()
                hairTreatmentPrice.text = servicesList[5].price.toString()
                hairWashPrice.text = servicesList[6].price.toString()
                fringePrice.text = servicesList[7].price.toString()
                beardPrice.text = servicesList[8].price.toString()


                chooseDate.setOnClickListener {
                    if (women.isChecked) {
                        priceSum += womenPrice.text.toString().toFloat()
                        timeSum += servicesList[0].time
                        chosenServices += "Women's haircut"
                    }
                    if (men.isChecked) {
                        priceSum += menPrice.text.toString().toFloat()
                        timeSum += servicesList[1].time
                        chosenServices += "Men's haircut"
                    }
                    if (coloring.isChecked) {
                        priceSum += coloringPrice.text.toString().toFloat()
                        timeSum += servicesList[2].time
                        chosenServices += "Coloring"
                    }
                    if (coloringColors.isChecked) {
                        priceSum += coloringColorsPrice.text.toString().toFloat()
                        timeSum += servicesList[3].time
                        chosenServices += "Coloring multiple colors"
                    }
                    if (decolorization.isChecked) {
                        priceSum += decolorizationPrice.text.toString().toFloat()
                        timeSum += servicesList[4].time
                        chosenServices += "Decolorization"
                    }
                    if (hairTreatment.isChecked) {
                        priceSum += hairTreatmentPrice.text.toString().toFloat()
                        timeSum += servicesList[5].time
                        chosenServices += "Hair care treatments"
                    }
                    if (hairWash.isChecked) {
                        priceSum += hairWashPrice.text.toString().toFloat()
                        timeSum += servicesList[6].time
                        chosenServices += "Washing and hair styling"
                    }
                    if (fringe.isChecked) {
                        priceSum += fringePrice.text.toString().toFloat()
                        timeSum += servicesList[7].time
                        chosenServices += "Fringe trimming"
                    }
                    if (beard.isChecked) {
                        priceSum += beardPrice.text.toString().toFloat()
                        timeSum += servicesList[8].time
                        chosenServices += "Beard trimming"
                    }

                    if(priceSum <= 0){
                        Toast.makeText(this, "Please choose at least 1 service", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val intent = Intent (this@NewVisit,MakeAppointment::class.java)

                        intent.putExtra("chosenServices", chosenServices)
                        intent.putExtra("priceSum", priceSum.toString())
                        intent.putExtra("timeSum", timeSum.toString())

                        startActivity(intent)
                        }


                    }

                }

            }

        women.setOnClickListener {
            if (women.isChecked) {
                men.isEnabled = false
                fringe.isEnabled = false
            } else {
                men.isEnabled = true
                fringe.isEnabled = true
            }
        }

        men.setOnClickListener {
            if (men.isChecked) {
                women.isEnabled = false
                fringe.isEnabled = false
            } else {
                women.isEnabled = true
                fringe.isEnabled = true
            }
        }

        coloring.setOnClickListener {
            if (coloring.isChecked) {
                coloringColors.isEnabled = false
                decolorization.isEnabled = false
            } else {
                coloringColors.isEnabled = true
                decolorization.isEnabled = true
            }
        }

        coloringColors.setOnClickListener {
            if (coloringColors.isChecked) {
                coloring.isEnabled = false
                decolorization.isEnabled = false
            } else {
                coloring.isEnabled = true
                decolorization.isEnabled = true
            }
        }

        decolorization.setOnClickListener {
            if (decolorization.isChecked) {
                coloring.isEnabled = false
                coloringColors.isEnabled = false
            } else {
                coloring.isEnabled = true
                coloringColors.isEnabled = true
            }
        }


        fringe.setOnClickListener {
            if (fringe.isChecked) {
                women.isEnabled = false
                men.isEnabled = false
            } else {
                women.isEnabled = true
                men.isEnabled = true
            }
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        openActivityDashboard()

    }

    private fun openActivityDashboard() {
        val intent = Intent(this,Dashboard::class.java)
        startActivity(intent)
    }

}






