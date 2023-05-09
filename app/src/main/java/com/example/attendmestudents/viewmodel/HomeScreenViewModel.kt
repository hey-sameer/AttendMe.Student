package com.example.attendmestudents.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendmestudents.model.ClassesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class HomeScreenViewModel@Inject constructor() : ViewModel() {
    val classId = mutableStateOf("")
    val classesIdList = mutableStateOf<List<String>>(emptyList())
    val enrolledClassesList = mutableStateOf<List<ClassesModel>>(emptyList())
    private var enrolledList = mutableListOf<ClassesModel>()
    private val studentDb = Firebase.firestore.collection("Students")
    private val classDb = Firebase.firestore.collection("Classes")
    private val auth = FirebaseAuth.getInstance()
    val check = mutableStateOf(true)
    init {
        getAllEnrolledClasses()
    }
    fun enrollInClass(onSuccess: ()-> Unit, onFailure:(String?) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        var idList = mutableListOf<String>()
        val studentQuery = studentDb.whereEqualTo("id",auth.uid).get().await()
        if(studentQuery.documents.isNotEmpty()){
            for(doc in studentQuery){
                idList = doc.get("classes") as MutableList<String>
            }
            for(classIds in idList){
                if(classIds == classId.value){
                    check.value = false
                    break
                }
            }
        }
        if(studentQuery.documents.isNotEmpty()&& check.value){
            for(doc in studentQuery){
                idList.add(classId.value)
                classesIdList.value = idList
                val classQuery = classDb.whereEqualTo("classId",classId.value).get().await()
                if(classQuery.documents.isNotEmpty()){
                    studentDb.document(doc.id).update("classes",classesIdList.value).addOnSuccessListener {
                        for(classes in classQuery){
                           var noOfStudent =  classes.get("noOfStudents").toString().toInt()
                            noOfStudent++
                            classDb.document(classes.id).update("noOfStudents",noOfStudent).addOnSuccessListener {
                                onSuccess()
                            }
                        }
                    }.addOnFailureListener {
                            onFailure(it.message)
                    }
                }else{
                    withContext(Dispatchers.Main){
                        onFailure("Invalid ID or Already Enrolled")
                    }
                }

            }
        }else{
            withContext(Dispatchers.Main){
                onFailure("Already Enrolled")
            }
        }
    }


    fun getAllEnrolledClasses() = CoroutineScope(Dispatchers.IO).launch {
        var idList = mutableListOf<String>()
        val studentQuery = studentDb.whereEqualTo("id",auth.uid).get().await()
        if(studentQuery.documents.isNotEmpty()){
            for(doc in studentQuery){
                idList = doc.get("classes") as MutableList<String>
            }
            for(classIds in idList){
                if(classIds == classId.value){
                    check.value = false
                    break
                }
            }
        }
        for(classId in idList){
            val classQuery  = classDb.whereEqualTo("classId",classId).get().await()
            if(classQuery.documents.isNotEmpty()){
                for(doc in classQuery){
                    var classes = ClassesModel(doc.get("batch").toString(),doc.get("className").toString(),doc.get("department").toString(),doc.get("profId").toString(),doc.get("classId").toString(),doc.get("noOfStudents").toString().toInt())
                    enrolledList.add(classes)
                }
                enrolledClassesList.value = enrolledList
            }
        }
    }
}