package com.example.attendmestudents.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendmestudents.model.DateAndTimeModel
import com.example.attendmestudents.model.AttendanceModel
import com.example.attendmestudents.model.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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
class QRScannerViewModel@Inject constructor(private val studentModel: StudentModel) : ViewModel(){
//aaaaaYYYY-MM-DDClassIDhh:mm:ss--hh:mm:ssaaaaa
    val isAttendanceInProgress = mutableStateOf(false)
    private val classId = mutableStateOf("")
    private val date = mutableStateOf("")
    val errorCode = mutableStateOf(0)
    //0 -> no error
    //1 invalid qr
    //2 -> expired qr
    private val generationTime = mutableStateOf("")
    private val expirationTime = mutableStateOf("")
    private val classDb = Firebase.firestore.collection("Classes")
    private val auth = FirebaseAuth.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    fun dataFromQR(qrCodeHex: String, onSuccess: ()-> Unit, onFailure: () -> Unit) {
        Log.d("@@QR", qrCodeHex)
        isAttendanceInProgress.value = true
        val qrCodeASCII = hexToASCII(qrCodeHex)
        if(parseQRCode(qrCodeASCII)) {
            addAttendance(onSuccess)
        }
        else{
            isAttendanceInProgress.value = false
            onFailure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseQRCode(qrCode: String): Boolean{
        if(qrCode.length != 44)
        {
            errorCode.value = 2
            return false
        }
        date.value = qrCode.substring(5, 15)
        classId.value = qrCode.substring(15, 21)
        generationTime.value = qrCode.substring(21, 29)
        expirationTime.value = qrCode.substring(31, 39)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now().format(formatter)
        if(currentDate == null){
            errorCode.value = 1
            return false
        }
        if (date.value != currentDate) {
            errorCode.value = 2
            return false
        }
        val currentTime = LocalTime.now()
        val startTime = LocalTime.parse(generationTime.value)
        val endTime = LocalTime.parse(expirationTime.value)
        if(currentTime == null || endTime == null)
        {
            errorCode.value = 1
            return false
        }
        Log.d("@Qr", "startTime $startTime , endTime $endTime")
        Log.d("@Qr", "class id ${classId.value} , date $currentDate")
        return if(currentTime.isAfter(startTime) && currentTime.isBefore(endTime))
            true
        else{
            errorCode.value = 2
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAttendance(onSuccess: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        if(classId.value.isNotEmpty()){
            val classQuery = classDb.whereEqualTo("classId",classId.value).get().await()
            if(classQuery.documents.isNotEmpty()){
                for(doc in classQuery){
                    val attendanceDb = Firebase.firestore.collection("Classes/${doc.id}/Attendance")
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val current = LocalDate.now().format(formatter)
                    val currStudent = AttendanceModel(auth.uid!!,studentModel.name,LocalDateTime.now().format(formatter))
                    val attendanceQuery = attendanceDb.whereEqualTo("date",current).get().await()
                    if(attendanceQuery.documents.isNotEmpty()){
                        var studentList = mutableListOf<AttendanceModel>()
                        for(doc in attendanceQuery){
                          studentList  = doc.get("studentList") as MutableList<AttendanceModel>
                        }
                        for(student in studentList){
                            if(student.id == auth.uid){
                                Log.d("@@Attendance", "Already marked")
                            }else{
                                studentList.add(currStudent)
                                for(doc in attendanceQuery){
                                    attendanceDb.document(doc.id).update("studentList",studentList).await()
                                    onSuccess()
                                }
                            }
                        }
                    }else{
                        var studentList = mutableListOf<AttendanceModel>()
                        studentList.add(currStudent)
                        val dateAndTime = DateAndTimeModel(current,studentList)
                        attendanceDb.add(dateAndTime)
                    }


//                    if(attendanceQuery.documents.isNotEmpty()){
//                        for(date in attendanceQuery){
//                            val addStudentListDb = Firebase.firestore.collection("Classes/${doc.id}/Attendance/${date.id}/StudentList")
//                            val studentQuery = addStudentListDb.whereEqualTo("id",auth.uid).get().await()
//                            if(studentQuery.documents.isNotEmpty()){
//                                Log.d("@@Attendance", "Already marked")
//                            }else{
//                                val currStudent = AttendanceModel(auth.uid!!,studentModel.name,LocalDateTime.now().format(formatter))
//                                addStudentListDb.add(currStudent).addOnSuccessListener {
//                                    onSuccess()
//                                }
//                            }
//                        }
//                    }
                }
            }
        }

    }

    private fun hexToASCII(hexValue: String): String {
        val output = StringBuilder("")
        var i = 0
        while (i < hexValue.length) {
            val str = hexValue.substring(i, i + 2)
            output.append(str.toInt(16).toChar())
            i += 2
        }
        return output.toString()
    }


}