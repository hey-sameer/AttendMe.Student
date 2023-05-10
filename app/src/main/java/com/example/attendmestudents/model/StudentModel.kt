package com.example.attendmestudents.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentModel(
    val id: String,
    val name: String,
    val email: String,
    val rollNo: String,
    val classes : List<String>
): Parcelable
