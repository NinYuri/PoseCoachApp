package com.example.posecoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.posecoach.data.repository.ProfileRepo
import com.example.posecoach.data.viewModel.EmPhViewModel
import com.example.posecoach.data.viewModel.ForgotPassViewModel
import com.example.posecoach.data.viewModel.LoginViewModel
import com.example.posecoach.data.viewModel.ProfileViewModel
import com.example.posecoach.data.viewModel.ProfileViewModelFactory
import com.example.posecoach.data.viewModel.RegistroViewModel
import com.example.posecoach.data.viewModel.UserViewModel
import com.example.posecoach.homeScreens.CameraScreen
import com.example.posecoach.homeScreens.HomeScreen
import com.example.posecoach.network.ApiClient
import com.example.posecoach.passwordScreens.Fields
import com.example.posecoach.passwordScreens.NewPasswordScreen
import com.example.posecoach.passwordScreens.OTPasswordScreen
import com.example.posecoach.profileScreens.AccountScreen
import com.example.posecoach.profileScreens.ChangeBirthday
import com.example.posecoach.profileScreens.EmailChange
import com.example.posecoach.profileScreens.EquipmentChange
import com.example.posecoach.profileScreens.ExperienceChange
import com.example.posecoach.profileScreens.GenderChange
import com.example.posecoach.profileScreens.GoalChange
import com.example.posecoach.profileScreens.HeightChange
import com.example.posecoach.profileScreens.PhoneChange
import com.example.posecoach.profileScreens.ProfileOTP
import com.example.posecoach.profileScreens.ProfileScreen
import com.example.posecoach.profileScreens.UserChange
import com.example.posecoach.profileScreens.VerificationType
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
fun MyApp(){
    val navController = rememberNavController()

    // VIEW MODELS
    val registroViewModel: RegistroViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val forgotPassViewModel: ForgotPassViewModel = viewModel()

    val repo = ProfileRepo(ApiClient.apiService)
    val profileViewModel: ProfileViewModel = viewModel( factory = ProfileViewModelFactory(repo))

    val emPhViewModel: EmPhViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ){
        // LOGIN
        composable("welcome") { WelcomeScreen(navController, loginViewModel) }

        // CONTRASEÑA
        composable("forgotPass") { Fields(navController, forgotPassViewModel) }
        composable("otpPass") { OTPasswordScreen(navController, forgotPassViewModel) }
        composable("newPass") { NewPasswordScreen(navController, forgotPassViewModel) }

        // REGISTRO
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

        // HOME
        composable ("home") { HomeScreen(navController) }
        composable ("camera") { CameraScreen(navController) }

        // PROFILE
        composable("profile") { ProfileScreen(navController, profileViewModel, loginViewModel) }
        composable("account") { AccountScreen(navController, profileViewModel) }
        composable( "changeUser" ) { UserChange(navController, profileViewModel, userViewModel) }
        composable(
            "changeEmail/{hasEmail}",
            listOf(navArgument("hasEmail") { type = NavType.BoolType })
        ){ backStackEntry ->
            val hasEmail = backStackEntry.arguments?.getBoolean("hasEmail") ?: false
            EmailChange(navController, hasEmail, profileViewModel, emPhViewModel)
        }
        composable(
            "changePhone/{hasPhone}",
            listOf(navArgument("hasPhone") { type = NavType.BoolType })
        ){ backStackEntry ->
            val hasPhone = backStackEntry.arguments?.getBoolean("hasPhone") ?: false
            PhoneChange(navController, hasPhone, profileViewModel, emPhViewModel)
        }
        composable(
            "profileOTP/{type}",
            listOf( navArgument("type") { type = NavType.StringType } )
        ){ backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: "EMAIL"
            ProfileOTP(navController, VerificationType.fromString(type), emPhViewModel)
        }
        composable("changeGender") { GenderChange(navController, profileViewModel) }
        composable("changeDate") { ChangeBirthday(navController, profileViewModel) }
        composable("changeHeight") { HeightChange(navController, profileViewModel) }
        composable("changeExp") { ExperienceChange(navController, profileViewModel) }
        composable("changeEquip") { EquipmentChange(navController, profileViewModel) }
        composable("changeGoal") { GoalChange(navController, profileViewModel) }
    }
}

