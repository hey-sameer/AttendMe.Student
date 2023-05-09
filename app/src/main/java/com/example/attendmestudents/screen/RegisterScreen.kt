package com.example.attendmestudents.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
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
import com.example.attendmestudents.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navHostController: NavHostController) {
    val viewModel: RegisterViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisible1 by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = viewModel.name.value,
                label = { Text(text = "Name") },
                onValueChange = { viewModel.name.value = it },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = viewModel.email.value,
                label = { Text(text = "Email") },
                onValueChange = {
                    viewModel.email.value = it
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = viewModel.rollNo.value,
                label = { Text(text = "RollNo") },
                placeholder = {Text(text = "BYYXXABC")},
                onValueChange = {
                    viewModel.rollNo.value = it
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )

            OutlinedTextField(value = viewModel.password.value,
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Minimum 6 chars") },
                isError = viewModel.password.value.length < 6,
                onValueChange = { viewModel.password.value = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (passwordVisible) com.example.attendmestudents.R.drawable.baseline_visibility_24 else com.example.attendmestudents.R.drawable.baseline_visibility_off_24
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(image), "")
                    }
                })
            OutlinedTextField(value = viewModel.rePassword.value,
                label = { Text(text = "Re-Password") },
                onValueChange = {
                    viewModel.rePassword.value = it
                },
                isError = viewModel.password.value != viewModel.rePassword.value,
                visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if(passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(image), "")
                    }
                })

            ClickableText(
                text = AnnotatedString("Login"),
                onClick = {
                    navHostController.navigate(Screens.LoginScreen.route) {
                        popUpTo(Screens.RegisterScreen.route) {
                            inclusive = true
                        }
                    }
                },
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            )

            ElevatedButton(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f, false),
                onClick = {
                    viewModel.register(
                        onSuccess = {
                            navHostController.navigate(Screens.HomeScreen.route) {
                                popUpTo(
                                    Screens.LoginScreen.route,
                                ) { inclusive = true }
                            }
                        },
                    ) {
                        Toast.makeText(
                            context,
                            "Some error: $it",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
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