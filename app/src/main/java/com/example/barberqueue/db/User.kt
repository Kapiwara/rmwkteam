package com.example.petcare.db

class User {
    val id: String
    val login: String

    constructor(id: String, login: String) {
        this.id = id
        this.login = login
    }

    constructor() {
        id = ""
        login = ""
    }

}