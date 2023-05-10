package com.example.attendmestudents.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendmestudents.navigation.DateAndTimeModel
import com.example.attendmestudents.navigation.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel@Inject constructor(val studentModel: StudentModel) : ViewModel(){
//aaaaaYYYY-MM-DDClassIDhh:mm:ss--hh:mm:ssaaaaa
    val classId = mutableStateOf("")
    val date = mutableStateOf("")
    val generationTime = mutableStateOf("")
    val expirationTime = mutableStateOf("")
    val valid = mutableStateOf(true)
    private val classDb = Firebase.firestore.collection("Classes")
    private val auth = FirebaseAuth.getInstance()
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAttendance() = CoroutineScope(Dispatchers.IO).launch {
        if(classId.value.isNotEmpty()){
            val classQuery = classDb.whereEqualTo("classId",classId.value).get().await()
            if(classQuery.documents.isNotEmpty()){
                for(doc in classQuery){
                    val attendanceDb = Firebase.firestore.collection("Classes/${doc.id}/Attendance")
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val current = LocalDateTime.now().format(formatter)
                    val dateAndTime = DateAndTimeModel(current)
                    attendanceDb.add(dateAndTime)
                    val attendanceQuery = attendanceDb.whereEqualTo("date",current).get().await()
                    if(attendanceQuery.documents.isNotEmpty()){
                        for(date in attendanceQuery){
                            val addStudentListDb = Firebase.firestore.collection("Classes/${doc.id}/Attendance/${date.id}/StudentList")
                            val studentQuery = addStudentListDb.whereEqualTo("id",auth.uid).get().await()
                            if(studentQuery.documents.isNotEmpty()){
                                Log.d("@@Attendance", "Already marked")
                            }else{
                                val currStudent = StudentModel(auth.uid!!,studentModel.studentName,LocalDateTime.now().format(formatter))
                                addStudentListDb.add(currStudent)
                            }
                        }
                    }
                }
            }
        }

    }
}