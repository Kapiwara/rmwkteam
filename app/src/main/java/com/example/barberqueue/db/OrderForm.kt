package com.example.barberqueue.db

import com.example.barberqueue.SummaryViewModel
import java.io.Serializable
import java.util.ArrayList

class OrderForm(
    val date: String? = null,
    val hour: String? = null,
    var isAccepted: Boolean = false,
    var isCanceled: Boolean = false,
    val isDone: Boolean = false,
    val price: Float = 0f,
    val services: ArrayList<SummaryViewModel>? = null,
    val servicesTime: Int = 0,
    val userId: String? = null
): Serializable {}

