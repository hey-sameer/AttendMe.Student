package com.example.attendmestudents.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.attendmestudents.model.StudentModel
import com.example.attendmestudents.screen.HomeScreen
import com.example.attendmestudents.screen.LoginScreen
import com.example.attendmestudents.screen.QrScannerScreen
import com.example.attendmestudents.screen.RegisterScreen
import com.example.attendmestudents.screen.SplashScreen
import com.example.attendmestudents.viewmodel.QRScannerViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetUpNavGraph(
    navHostController: NavHostController,
    context: Context,
    lifecycleOwner: LifecycleOwner
){
    NavHost(navController = navHostController, startDestination = Screens.SplashScreen.route ){
        composable(route = Screens.SplashScreen.route){
            SplashScreen(navHostController)
        }
        composable(route = Screens.LoginScreen.route){
            LoginScreen(navHostController)
        }
        composable(route = Screens.RegisterScreen.route){
            RegisterScreen(navHostController)
        }
        composable(route = Screens.HomeScreen.route){
            HomeScreen(navHostController)
        }
        composable(route = Screens.QRScannerScreen.route){
            val student = navHostController.previousBackStackEntry?.savedStateHandle?.get<StudentModel>("studentModel")
            val qrViewModel: QRScannerViewModel = viewModel(initializer = {QRScannerViewModel(student!!, context)})
            QrScannerScreen(qrViewModel,navHostController)
        }
    }
}