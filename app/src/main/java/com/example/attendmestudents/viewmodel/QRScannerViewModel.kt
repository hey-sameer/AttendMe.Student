package com.example.attendmestudents.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel@Inject constructor() : ViewModel(){
//aaaaaYYYY-MM-DDClassIDhh:mm:ss--hh:mm:ssaaaaa
    val classId = mutableStateOf("")
    val date = mutableStateOf("")
    val generationTime = mutableStateOf("")
    val expirationTime = mutableStateOf("")
    val valid = mutableStateOf(true)
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseQRCode(qrCode : String): Boolean {
        if(qrCode.length == 42){
            if(qrCode[9] != '-' || qrCode[12] != '-' || qrCode[23] != ':' || qrCode[26] != '-' || qrCode[29] != '-' || qrCode[30] != '-' || qrCode[33] != ':' || qrCode[36] != ':'){
               valid.value = false
            }else{
                classId.value = qrCode.substring(15,19)
                date.value = qrCode.substring(5,13)
                generationTime.value = qrCode.substring(21,27)
                expirationTime.value = qrCode.substring(31,37)
                var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                var current = LocalDate.now().format(formatter)
                if(date.value!=current){
                    valid.value = false
                }
                val currentTime = LocalTime.now()
                val startTime = LocalTime.parse(generationTime.value)
                val endTime = LocalTime.parse(expirationTime.value)
                valid.value = currentTime.isAfter(startTime) && currentTime.isBefore(endTime)
            }
        }
        return valid.value
    }

    fun addAttendance() = CoroutineScope(Dispatchers.IO).launch {

    }
}