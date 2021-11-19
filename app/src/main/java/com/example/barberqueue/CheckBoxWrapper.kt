package com.example.barberqueue

import android.widget.CheckBox
//to jest klasa pudelko na checkboxa zeby mial tez czas i cene (chodzi o te uslugi ktore sie checkboxuje zeby potem przeslac do dokumentu z klasy OrderForm)
data class CheckBoxWrapper(
    var checkBox: CheckBox? = null,
    var time: Int = 0,
    var price: Int = 0

)
