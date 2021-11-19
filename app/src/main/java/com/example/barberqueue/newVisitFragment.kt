package com.example.barberqueue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.barberqueue.databinding.DashboardBinding


class newVisitFragment : Fragment() {
    lateinit var boys: CheckBoxWrapper
    lateinit var girls: CheckBox
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

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_new_visit,container,false)
        boys = CheckBoxWrapper(view.findViewById(R.id.checkBox_boys_haircut),20,35) // ceny i czas uslugi trzeba bedzie pobierac z firebase
        girls = view.findViewById(R.id.checkBox_girls_haircut)
        women = view.findViewById(R.id.checkBox_women_haircut)
        men = view.findViewById(R.id.checkBox_men_haircut)
        coloringHaircut = view.findViewById(R.id.checkBox_coloring_and_haircut)
        coloringColors = view.findViewById(R.id.checkBox_coloring_more_than_1_color)
        decolorization = view.findViewById(R.id.checkBox_decolorization)
        hairTreatment = view.findViewById(R.id.checkBox_hair_care_treatments)
        hairWash = view.findViewById(R.id.checkBox_washing_and_hair_styling)
        fringe = view.findViewById(R.id.checkBox_fringe_trimming)
        beard = view.findViewById(R.id.checkBox_beard_trimming)



        return view
    }




}