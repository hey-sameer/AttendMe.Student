package com.example.attendmestudents.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor() : ViewModel(){
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val status = mutableStateOf(false)
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    fun login(onSuccess:() -> Unit, onFailure: (String?) -> Unit) = CoroutineScope(Dispatchers.IO).launch{
        auth.signInWithEmailAndPassword(email.value,password.value).addOnSuccessListener {
            status.value = true
            onSuccess()
        }.addOnFailureListener {
            onFailure(it.message)
            status.value = false
        }
    }
}