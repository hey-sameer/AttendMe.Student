package com.example.attendmestudents.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendmestudents.screen.LoginScreen
import com.example.attendmestudents.screen.RegisterScreen

@Composable
fun SetUpNavGraph(
    navHostController: NavHostController,
    context: Context,
    lifecycleOwner: LifecycleOwner
){
    NavHost(navController = navHostController, startDestination = Screens.LoginScreen.route ){
        composable(route = Screens.LoginScreen.route){
            LoginScreen(navHostController)
        }
        composable(route = Screens.RegisterScreen.route){
            RegisterScreen(navHostController)
        }
        composable(route = Screens.HomeScreen.route){
//            HomeScreen(navHostController)
        }
    }
}