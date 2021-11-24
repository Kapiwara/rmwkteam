package com.example.barberqueue.db

import java.io.Serializable

data class Service(var name: String? = null,var price: Float = 0.0F, var time: Int = 0): Serializable{
}
