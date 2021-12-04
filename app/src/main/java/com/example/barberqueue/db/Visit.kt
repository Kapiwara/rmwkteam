package com.example.barberqueue.db

import java.io.Serializable
import java.util.*

data class Visit(val listOfServices: List<Int>, val price: Float, val date: Date) : Serializable {
}