package com.example.posecoach.data.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.model.UpdateRequest
import com.example.posecoach.data.repository.ProfileRepo
import com.example.posecoach.data.responses.ProfileResponse
import com.example.posecoach.data.responses.UserProfileResponse
import com.example.posecoach.data.token.TokenManager
import com.example.posecoach.network.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepo): ViewModel() {
    var loading = mutableStateOf(false)
    var error = mutableStateOf("")
    var mensaje = mutableStateOf("")
    var isDelete = mutableStateOf(false)
    var userProfile = mutableStateOf<UserProfileResponse?>(null)

    // Opciones del perfil
    var selectedEmail = mutableStateOf("")
    var selectedPhone = mutableStateOf("")
    var selectedUsername = mutableStateOf("")
    var selectedDate = mutableStateOf("")
    var selectedSex = mutableStateOf("")
    var selectedHeight = mutableStateOf(0)
    var selectedExp = mutableStateOf("")
    var selectedGoal = mutableStateOf("")
    var selectedEquip = mutableStateOf("")

    val updateResult = mutableStateOf<String?>(null)

    private val api = ApiClient.apiService

    fun getUserProfile() {
        loading.value = true
        error.value = ""

        viewModelScope.launch {
            try {
                val response = api.getUserProfile()

                if(response.isSuccessful) {
                    userProfile.value = response.body()

                    response.body()?.user?.let { profile ->
                        selectedEmail.value = profile.email ?: ""
                        selectedPhone.value = profile.phone ?: ""
                        selectedUsername.value = profile.username
                        selectedDate.value = profile.dateBirth
                        selectedSex.value = profile.sex
                        selectedHeight.value = profile.height
                        selectedExp.value = profile.experience
                        selectedGoal.value = profile.goal
                        selectedEquip.value = profile.equipment
                    }
                } else
                    error.value = "Error al obtener el perfil: ${response.code()}"
            } catch(e: Exception) {
                error.value = "Error de conexión: ${e.message}"
            }
            loading.value = false
        }
    }

    fun updateProfile(request: UpdateRequest) {
        loading.value = true
        updateResult.value = null

        viewModelScope.launch {
            try {
                val response = repository.updateProfile(request)

                if(response.isSuccessful) {
                    val body = response.body()
                    updateResult.value = body?.mensaje ?: "Perfil actualizado exitosamente."
                } else {
                    val errorJson = response.errorBody()?.string()
                    val gson = Gson()

                    val errorResponse = gson.fromJson(errorJson, ProfileResponse::class.java)
                    updateResult.value =
                        errorResponse.error?.values?.firstOrNull()?.firstOrNull()
                            ?: "Error desconocido."
                }
            } catch (e: Exception) {
                updateResult.value = "Error de conexión ${e.message}"
            }
            loading.value = false
        }
    }

    fun deleteUser(loginViewModel: LoginViewModel? = null) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val response = api.deleteUser()

                if(response.isSuccessful) {
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "Cuenta eliminada exitosamente."
                    isDelete.value = true

                    TokenManager.clearTokens()
                    loginViewModel?.logout()
                } else {
                    error.value = "Lo lamento, ocurrió un error."
                    isDelete.value = false
                }
            } catch (e: Exception) {
                error.value = "Error de conexión: ${e.message}"
                isDelete.value = false
            }
            loading.value = false
        }
    }

    fun clearMessages() {
        mensaje.value = ""
        error.value = ""
    }
}