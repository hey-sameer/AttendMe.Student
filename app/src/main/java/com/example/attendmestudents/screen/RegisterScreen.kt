package com.example.attendmestudents.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendmestudents.R
import com.example.attendmestudents.navigation.Screens
import com.example.attendmestudents.ui.theme.whiteBackground
import com.example.attendmestudents.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navHostController: NavHostController) {
    val viewModel: RegisterViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisible1 by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val image = painterResource(id = R.drawable.register_page_removebg_preview)

    Surface() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(image, "")
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sign Up",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = viewModel.name.value,
                        label = { Text(text = "Name") },
                        onValueChange = { viewModel.name.value = it },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text)
                    )
                    OutlinedTextField(
                        value = viewModel.email.value,
                        label = { Text(text = "Email") },
                        onValueChange = {
                            viewModel.email.value = it
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email)
                    )
                    OutlinedTextField(
                        value = viewModel.rollNo.value,
                        label = { Text(text = "RollNo") },
                        placeholder = { Text(text = "BYYXXABC") },
                        onValueChange = {
                            viewModel.rollNo.value = it
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Ascii)
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = viewModel.password.value,
                        label = { Text(text = "Password") },
                        placeholder = { Text(text = "Minimum 6 chars") },
                        isError = viewModel.password.value.length < 6,
                        onValueChange = { viewModel.password.value = it },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                                    contentDescription = "",
                                    tint = if (passwordVisible) MaterialTheme.colorScheme.primary else Color.Gray,
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Password)
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = viewModel.rePassword.value,
                        label = { Text(text = "Re-Password") },
                        onValueChange = {
                            viewModel.rePassword.value = it
                        },
                        isError = viewModel.password.value != viewModel.rePassword.value,
                        visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible1 = !passwordVisible1
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                                    contentDescription = "",
                                    tint = if (passwordVisible1) MaterialTheme.colorScheme.primary else Color.Gray,
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password)
                    )


                    Spacer(modifier = Modifier.padding(10.dp))
                    ElevatedButton(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .weight(1f, false),
                        enabled = viewModel.name.value.isNotBlank() && viewModel.email.value.isNotBlank() && viewModel.rollNo.value.isNotBlank() && viewModel.password.value.length >= 6 && viewModel.rePassword.value == viewModel.password.value,
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
                    ) {
                        Text(
                            text = "Sign Up",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(IntrinsicSize.Max)
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Text(
                        text = "Login Instead",
                        modifier = Modifier.clickable(onClick = {
                            navHostController.navigate(Screens.LoginScreen.route) {
                                popUpTo = navHostController.graph.startDestinationId
                                launchSingleTop = true
                            }
                        })
                    )
                    Spacer(modifier = Modifier.padding(0.dp))
                }
            }
        }
    }
}