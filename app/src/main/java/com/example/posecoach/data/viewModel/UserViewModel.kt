package com.example.posecoach.data.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.model.CheckUsername
import com.example.posecoach.data.model.RegistroRequest
import com.example.posecoach.data.model.RegistroUsuario
import com.example.posecoach.data.model.ResendOtp
import com.example.posecoach.data.model.VerifyOTP
import com.example.posecoach.data.responses.CompleteResponse
import com.example.posecoach.data.responses.OtpResponse
import com.example.posecoach.data.responses.RegisterResponse
import com.example.posecoach.data.responses.ResendOtpResponse
import com.example.posecoach.data.responses.UsernameResponse
import com.example.posecoach.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel: ViewModel() {
    // Estados de UI
    var loading = mutableStateOf(false)
    var mensaje = mutableStateOf("")
    var error = mutableStateOf("")

    //  Datos guardados
    var temporalId = mutableStateOf(0)
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")
    var usernameAvailable = mutableStateOf(false)
    var checkUsername = mutableStateOf(false)
    var usernameMessage = mutableStateOf("")

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
                    error.value = Errores(errorBody) ?: "Error desconocido"
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
                    error.value = Errores(errorBody) ?: "Error al verificar OTP"
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
                    error.value = Errores(errorBody) ?: "Error al reenviar código OTP"
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    // Revisar nombre de usuario
    fun checkUsername(username: String) {
        checkUsername.value = true
        usernameMessage.value = ""
        usernameAvailable.value = false

        viewModelScope.launch {
            try {
                val response: Response<UsernameResponse> = api.verifyUsername(
                    CheckUsername(username = username)
                )

                if(response.isSuccessful) {
                    val body = response.body()
                    usernameAvailable.value = body?.available?.toBoolean() ?: false
                    usernameMessage.value = body?.mensaje ?: ""
                } else {
                    val errorBody = response.errorBody()?.string()
                    usernameMessage.value = Errores(errorBody) ?: "Error al verificar nombre de usuario."
                    usernameAvailable.value = false
                }
            } catch (e: Exception) {
                usernameMessage.value = e.message ?: "Error de conexión"
                usernameAvailable.value = false
            }
            checkUsername.value = false
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
                    error.value = Errores(errorBody) ?: "Error al completar el perfil"
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
    private fun Errores(errorBody: String?): String? {
        return try {
            if(errorBody.isNullOrEmpty()) return "Lo siento, ocurrió un error desconocido."

            // 1. Buscar patrones para email y phone (register)
            val emailPattern = """\"email\":\s*\[\"([^\"]+)\"\]""".toRegex()
            val phonePattern = """\"phone\":\s*\[\"([^\"]+)\"\]""".toRegex()

            val emailMatch = emailPattern.find(errorBody)
            if(emailMatch != null)
                return emailMatch.groupValues[1]

            val phoneMatch = phonePattern.find(errorBody)
            if(phoneMatch != null)
                return phoneMatch.groupValues[1]

            // 2. Buscar error simple ({"error": "mensaje"})
            val errorSimple = """\"error\":\s*\"([^\"]+)\"""".toRegex()
            val errorSimpleMatch = errorSimple.find(errorBody)

            if(errorSimpleMatch != null)
                return errorSimpleMatch.groupValues[1]

            // 3. Buscar error en formato de OTP expirado
            val otpError = """\"error\":\s*\"([^\"]+)\"(?:,\s*\"sugerencia\":\s*\"([^\"]+)\")?""".toRegex()
            val otpErrorMatch = otpError.find(errorBody)

            if(otpErrorMatch != null) {
                val errorMessage = otpErrorMatch.groupValues[1]
                return errorMessage
            }

            // 4. Si no encuentra ninguno, devolver errorBody limpio
            return limpiarJson(errorBody)
        } catch(e: Exception) {
            errorBody ?: "Lo siento, ocurrió un error desconocido."
        }
    }

    private fun limpiarJson(jsonString: String): String {
        return try {
            var cleaned = jsonString
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .replace(Regex("\\s+"), " ")
                .trim()

            if(cleaned.isEmpty() || cleaned == "error:")
                "Error en el servidor. Por favor, intenta nuevamente."
            else
                cleaned.replaceFirstChar { it.uppercase() }
        } catch(e: Exception) {
            "Error en el servidor. Por favor, intenta nuevamente."
        }
    }

    // Limpiar estados
    fun clearMessages() {
        mensaje.value = ""
        error.value = ""
    }

    fun clearUsername() {
        usernameAvailable.value = false
        usernameMessage.value = ""
        checkUsername.value = false
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