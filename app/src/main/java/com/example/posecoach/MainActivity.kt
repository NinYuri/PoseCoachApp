package com.example.posecoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.posecoach.data.viewModel.LoginViewModel
import com.example.posecoach.data.viewModel.RegistroViewModel
import com.example.posecoach.data.viewModel.UserViewModel
import com.example.posecoach.userScreens.BirthdayScreen
import com.example.posecoach.userScreens.EquipmentScreen
import com.example.posecoach.userScreens.ExperienceScreen
import com.example.posecoach.userScreens.GenderScreen
import com.example.posecoach.userScreens.GoalScreen
import com.example.posecoach.userScreens.HeightScreen
import com.example.posecoach.userScreens.OTPScreen
import com.example.posecoach.userScreens.RegisterScreen
import com.example.posecoach.userScreens.UsernameScreen
import com.example.posecoach.userScreens.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val registroViewModel: RegistroViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ){
        composable("welcome") { WelcomeScreen(navController, loginViewModel) }

        composable("register") { RegisterScreen(navController, userViewModel) }
        composable ("otpcode")  {
            val temporalId = it.arguments?.getString("temporalId")?.toIntOrNull() ?: 0
            OTPScreen(navController, userViewModel, temporalId)
        }
        composable ("username") {
            val temporalId = it.arguments?.getString("temporalId")?.toIntOrNull() ?: 0
            UsernameScreen(navController, registroViewModel, userViewModel, temporalId)
        }
        composable ("gender") { GenderScreen(navController, registroViewModel) }
        composable ("birthday") { BirthdayScreen(navController, registroViewModel) }
        composable ("height") { HeightScreen(navController, registroViewModel) }
        composable ("goal") { GoalScreen(navController, registroViewModel) }
        composable ("experience") { ExperienceScreen(navController, registroViewModel) }
        composable ("equipment") { EquipmentScreen(navController, registroViewModel, userViewModel) }

        composable ("home") { HomeScreen(navController) }
    }
}

