package com.example.barberqueue.db

import java.io.Serializable

data class Contact(val about_us: String = "", val contact_adress: String ="", val contact_phone: String=""): Serializable {
}