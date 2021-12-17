package com.example.barberqueue.interfaces

import com.example.barberqueue.db.OrderForm
import com.google.firebase.firestore.auth.User


interface FromMakeAppointmentToSummary {
    fun getSelectedTime(time: String)
}

interface OrderClickView{
    fun onClickOrder(orderForm : OrderForm, position: Int)
}