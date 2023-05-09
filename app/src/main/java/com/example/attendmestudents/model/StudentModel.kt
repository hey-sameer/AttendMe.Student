package com.example.attendmestudents.model

data class StudentModel(
    val id: String,
    val name: String,
    val email: String,
    val rollNo: String,
    val classes : List<String>
)
