package com.example.barberqueue.db

import com.example.barberqueue.SummaryViewModel
import java.io.Serializable
import java.util.ArrayList

class OrderForm(
    val date: String? = null,
    val hour: String? = null,
    val isAccepted: Boolean = false,
    val isCanceled: Boolean = false,
    val isDone: Boolean = false,
    val price: Float = 0f,
    val services: ArrayList<SummaryViewModel>? = null,
    val servicesTime: Int = 0,
    val userId: String? = null
): Serializable {}

