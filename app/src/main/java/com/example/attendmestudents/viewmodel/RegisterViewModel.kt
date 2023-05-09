package com.example.attendmestudents.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendmestudents.model.StudentModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(): ViewModel() {
    val name = mutableStateOf("")
    val rollNo = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val rePassword = mutableStateOf("")
    private val db = Firebase.firestore.collection("Students")

    fun register(onSuccess: () -> Unit, onFailure: (String?) -> Unit){
        if(name.value.isNotBlank() && rollNo.value.isNotBlank() && email.value.isNotBlank() && password.value.length >= 6 && rePassword.value == password.value)
        {
            Firebase.auth.createUserWithEmailAndPassword(email.value,password.value).addOnSuccessListener {
                val student = StudentModel(Firebase.auth.currentUser!!.uid, name.value, email.value, rollNo.value, listOf())

                db.add(student).addOnSuccessListener {
                    onSuccess()
                }
            }.addOnFailureListener{
                onFailure(it.message)
            }
        }
    }
}