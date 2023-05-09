package com.example.attendmestudents.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendmestudents.viewmodel.HomeScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController){
    val viewModel : HomeScreenViewModel = viewModel()
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = viewModel.classId.value,
                    label = { Text(text = "Enter Class ID") },
                    onValueChange = {
                        viewModel.classId.value = it
                    },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    ElevatedButton(
                        modifier = Modifier
                            .padding(5.dp),
                        onClick = {
                            viewModel.enrollInClass(
                                onSuccess = {
                                    viewModel.classId.value = ""
                                },
                                onFailure = {
                                    Toast.makeText(
                                        context,
                                        "Some error: $it",
                                        Toast.LENGTH_LONG,
                                    ).show()
                                },
                            )
                        },
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text(
                            text = "+", modifier = Modifier
                                .padding(8.dp)
                                .width(IntrinsicSize.Max)
                        )
                    }
                }
            }

        }
    }
}