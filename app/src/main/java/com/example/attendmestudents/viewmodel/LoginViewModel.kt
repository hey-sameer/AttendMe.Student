package com.example.attendmestudents.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    fun login(onSuccess:() -> Unit, onFailure: (String?) -> Unit) {
        if(email.value.isEmpty() || password.value.length < 6)
        {
            onFailure("Please fill the boxes correctly")
        }
        viewModelScope.launch(Dispatchers.IO){
            auth.signInWithEmailAndPassword(email.value,password.value).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailure(it.message)
            }
        }
    }
}