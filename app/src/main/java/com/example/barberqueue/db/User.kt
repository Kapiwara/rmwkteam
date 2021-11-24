package com.example.barberqueue.db

import java.io.Serializable

data class User(val login: String = "", val name: String ="", val phone: String =""): Serializable {
}