package com.example.barberqueue.db

import java.io.Serializable

data class Settings(var further_days: Int = 14): Serializable {
}