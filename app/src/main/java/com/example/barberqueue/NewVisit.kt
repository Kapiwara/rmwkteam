package com.example.barberqueue

import android.os.Bundle
import android.util.Log
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
        Log.e("abc", "dupka")


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
        val coloringHaircut = findViewById<CheckBox>(R.id.checkBox_coloring_and_haircut)
        val coloringColors = findViewById<CheckBox>(R.id.checkBox_coloring_more_than_1_color)
        val decolorization = findViewById<CheckBox>(R.id.checkBox_decolorization)
        val hairTreatment = findViewById<CheckBox>(R.id.checkBox_hair_care_treatments)
        val hairWash = findViewById<CheckBox>(R.id.checkBox_washing_and_hair_styling)
        val fringe = findViewById<CheckBox>(R.id.checkBox_fringe_trimming)
        val beard = findViewById<CheckBox>(R.id.checkBox_beard_trimming)


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
                coloringHaircut.text = servicesList[2].name.toString()
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

            }

        }


    }


}






