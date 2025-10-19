package com.example.posecoach.data.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.model.RegistroRequest
import com.example.posecoach.data.model.RegistroUsuario
import com.example.posecoach.data.model.ResendOtp
import com.example.posecoach.data.model.VerifyOTP
import com.example.posecoach.data.responses.CompleteResponse
import com.example.posecoach.data.responses.OtpResponse
import com.example.posecoach.data.responses.RegisterResponse
import com.example.posecoach.data.responses.ResendOtpResponse
import com.example.posecoach.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel: ViewModel() {
    // Estados de UI
    var loading = mutableStateOf(false)
    var mensaje = mutableStateOf("")
    var error = mutableStateOf("")

    // Temporal ID - datos guardados
    var temporalId = mutableStateOf(0)
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")

    // Estados específicos para cada operación
    var registerSuccess = mutableStateOf(false)
    var otpVerified = mutableStateOf(false)
    var profileCompleted = mutableStateOf(false)

    // Retrofit API
    private val api = ApiClient.apiService

    // Registro inicial
    fun registerInitial(registerData: RegistroRequest) {
        loading.value = true
        error.value = ""
        viewModelScope.launch {
            try {
                val response: Response<RegisterResponse> = api.registerUser(registerData)

                if(response.isSuccessful) {
                    val body = response.body()
                    temporalId.value = body?.temporal_id ?: 0
                    mensaje.value = body?.mensaje ?: "Registro exitoso"
                    error.value = ""
                    registerSuccess.value = true

                    userEmail.value = registerData.email
                    userPhone.value = registerData.phone
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody) ?: "Error desconocido"
                    registerSuccess.value = false
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
                registerSuccess.value = false
            }
            loading.value = false
        }
    }

    // Verificar OTP
    fun verifyOtp(otpData: VerifyOTP) {
        loading.value = true
        error.value = ""
        viewModelScope.launch {
            try {
                val response: Response<OtpResponse> = api.verifyOtp(otpData)

                if(response.isSuccessful) {
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "OTP verificado exitosamente"
                    error.value = ""
                    otpVerified.value = true

                    // Actualizar temporalId si viene en la respuesta
                    body?.temporal_id?.let {
                        temporalId.value = it
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody) ?: "Error al verificar OTP"
                    otpVerified.value = false
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
                otpVerified.value = false
            }
            loading.value = false
        }
    }

    // Reenviar OTP
    fun resendOtp(resendData: ResendOtp) {
        loading.value = true
        error.value = ""
        viewModelScope.launch {
            try {
                val response: Response<ResendOtpResponse> = api.resendOtp(resendData)

                if(response.isSuccessful){
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "Código OTP reenviado exitosamente"
                    error.value = ""

                    body?.temporal_id?.let {
                        temporalId.value = it
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody) ?: "Error al reenviar código OTP"
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    // Completar perfil
    fun completeProfile(userData: RegistroUsuario) {
        loading.value = true
        error.value = ""
        viewModelScope.launch {
            try {
                val response: Response<CompleteResponse> = api.completeUser(userData)

                if(response.isSuccessful) {
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "Perfil completado exitosamente"
                    error.value = ""
                    profileCompleted.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody) ?: "Error al completar el perfil"
                    profileCompleted.value = false
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
                profileCompleted.value = false
            }
            loading.value = false
        }
    }

    // Función para parsear errores de la API
    private fun parseError(errorBody: String?): String? {
        return try {
            errorBody
        } catch(e: Exception) {
            "Error al procesar la respuesta"
        }
    }

    // Limpiar estados
    fun clearMessages() {
        mensaje.value = ""
        error.value = ""
    }

    fun clearAllStates() {
        loading.value = false
        mensaje.value = ""
        error.value = ""
        registerSuccess.value = false
        otpVerified.value = false
        profileCompleted.value = false
    }
}