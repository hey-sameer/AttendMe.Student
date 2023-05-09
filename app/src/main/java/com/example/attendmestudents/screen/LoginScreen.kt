package com.example.attendmestudents.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendmestudents.R
import com.example.attendmestudents.navigation.Screens
import com.example.attendmestudents.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navHostController: NavHostController){
    val viewModel : LoginViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            OutlinedTextField(
                value = viewModel.email.value,
                label = { Text(text = "Email") },
                onValueChange = {
                    viewModel.email.value = it
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )

            OutlinedTextField(
                value = viewModel.password.value,
                label = { Text(text = "Password") },
                onValueChange = {
                    viewModel.password.value = it
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if(passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(image), "")
                    }
                }
            )
            ClickableText(
                text = AnnotatedString("or Register"),
                onClick = { navHostController.navigate(Screens.RegisterScreen.route){
                    popUpTo(route = Screens.LoginScreen.route){
                        inclusive = true
                    }
                } },
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            )
            Box(modifier = Modifier.fillMaxSize()) {
                ElevatedButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp),
                    onClick = {
                        viewModel.login(
                            onSuccess = {
                                navHostController.navigate(Screens.HomeScreen.route) {
                                    popUpTo(
                                        Screens.LoginScreen.route,
                                    ) { inclusive = true }
                                }
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
                        text = "Continue", modifier = Modifier
                            .padding(8.dp)
                            .width(IntrinsicSize.Max)
                    )
                }
            }
        }
    }

}