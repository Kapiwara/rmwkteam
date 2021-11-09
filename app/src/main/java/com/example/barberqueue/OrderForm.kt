package com.example.barberqueue

data class OrderForm (
    var date: String? = null,
    var isAccepted: Boolean = false,
    var isCanceled: Boolean = false,
    var isDone: Boolean = false,
    var price: Int =0,
    var services: String? = null ,
    var servicesTime: Int = 0,
    var userId: String? = null
)