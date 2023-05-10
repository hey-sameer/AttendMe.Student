package com.example.attendmestudents.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendmestudents.R
import com.example.attendmestudents.navigation.Screens
import com.example.attendmestudents.ui.theme.whiteBackground
import com.example.attendmestudents.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val image = painterResource(id = R.drawable.login_image)
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), contentAlignment = Alignment.TopCenter
        ) {

            Image(
                image,
                "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(300.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .background(whiteBackground)
                .padding(10.dp)
        ) {

            Text(
                text = "Sign In",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = viewModel.email.value,
                    onValueChange = { viewModel.email.value = it },
                    label = { Text(text = "Email Address") },
                    placeholder = { Text(text = "Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )

                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.password.value = it },
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
                    label = { Text("Password") },
                    placeholder = { Text(text = "Min 6 character") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .focusRequester(focusRequester = focusRequester),
                )

                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    enabled = viewModel.email.value.isNotBlank() && viewModel.password.value.length >= 6,
                    onClick = {
                        viewModel.login(
                            onSuccess = {
                                navController.navigate(Screens.HomeScreen.route) {
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
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(text = "Sign In", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "Create An Account",
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screens.RegisterScreen.route) {
                            popUpTo(route = Screens.LoginScreen.route) {
                                inclusive = true
                            }
                        }
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
            }


        }
    }

}


/*
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
 */