package com.example.posecoach.data.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.model.ForgotPassEmailRequest
import com.example.posecoach.data.model.ForgotPassPhoneRequest
import com.example.posecoach.data.model.ResendOtp
import com.example.posecoach.data.model.ResetPassEmailRequest
import com.example.posecoach.data.model.ResetPassPhoneRequest
import com.example.posecoach.data.responses.ForgotPassResponse
import com.example.posecoach.data.responses.ResendOtpResponse
import com.example.posecoach.data.responses.ResetPassResponse
import com.example.posecoach.network.ApiClient
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import retrofit2.Response

class ForgotPassViewModel: ViewModel() {
    var loading = mutableStateOf(false)
    var mensaje = mutableStateOf("")
    var error = mutableStateOf("")

    // Datos guardados
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")
    var userOTP = mutableStateOf("")

    // Estados
    var userVerified = mutableStateOf(false)
    var reset = mutableStateOf(false)

    // Contraseña
    var passEmail = mutableStateOf(ResetPassEmailRequest())
    var passPhone = mutableStateOf(ResetPassPhoneRequest())

    private val api = ApiClient.apiService

    // Olvidar contraseña
    fun forgotPassByEmail(email: String) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = ForgotPassEmailRequest(email = email)
                val response: Response<ForgotPassResponse> = api.forgotPassEmail(request)

                handleResponse(response)
                userEmail.value = email
                Log.d("Backend", userOTP.value)
            } catch (e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun forgotPassByPhone(phone: String) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = ForgotPassPhoneRequest(phone = phone)
                val response: Response<ForgotPassResponse> = api.forgotPassPhone(request)

                handleResponse(response)
                userPhone.value = phone
                Log.d("Backend", userOTP.value)
            } catch (e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    private fun handleResponse(response: Response<ForgotPassResponse>) {
        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                if (body.mensaje != null) {
                    mensaje.value = body.mensaje
                    userOTP.value = body.otp ?: ""
                    error.value = ""
                    userVerified.value = true
                } else if (body.error != null) {
                    error.value = body.error
                    mensaje.value = ""
                    userVerified.value = false
                } else {
                    error.value = "Respuesta inesperada del servidor."
                    userVerified.value = false
                }
            } else {
                error.value = "Lo siento, recibí una respuesta vacía del servidor."
                userVerified.value = false
            }
        } else {
            val errorBody = response.errorBody()?.string()
            error.value = parseError(errorBody) ?: "Error desconocido. Por favor, inténtelo de nuevo."
            userVerified.value = false
        }
    }

    // Verificar OTP
    fun verifyOTP(otp: String): Boolean {
        mensaje.value = "OTP verificado exitosamente"
        return otp == userOTP.value
    }

    // Reenviar OTP
    fun resendOTP(resendData: ResendOtp) {
        loading.value = true
        error.value = ""
        viewModelScope.launch {
            try {
                val response: Response<ResendOtpResponse> = api.resendOTPass(resendData)

                if(response.isSuccessful){
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "Código OTP reenviado exitosamente"
                    error.value = ""
                    userOTP.value = body?.otp ?: ""
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

    // Resetear contraseña
    fun resetPassEmail(passData: ResetPassEmailRequest) {
        loading.value = true
        error.value = ""

        viewModelScope.launch {
            try {
                val response: Response<ResetPassResponse> = api.resetPassEmail(passData)

                if(response.isSuccessful) {
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "Contraseña restablecida correctamente"
                    error.value = ""
                    reset.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody)
                    reset.value = false
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
                reset.value = false
            }
            loading.value = false
        }
    }

    fun resetPassPhone(passData: ResetPassPhoneRequest) {
        loading.value = true
        error.value = ""

        viewModelScope.launch {
            try {
                val response: Response<ResetPassResponse> = api.resetPassPhone(passData)

                if(response.isSuccessful) {
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "Contraseña restablecida correctamente"
                    error.value = ""
                    reset.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody)
                    reset.value = false
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
                reset.value = false
            }
            loading.value = false
        }
    }


    private fun parseError(errorBody: String?): String {
        if (errorBody.isNullOrEmpty())
            return "Lo siento, hubo un error desconocido."

        return try {
            val jsonElement = JsonParser().parse(errorBody)
            val jsonObject = jsonElement.asJsonObject

            // 1. Estructura { "error": { "non_field_errors": ["mensaje" ] } }
            if (jsonObject.has("error")) {
                val errorObj = jsonObject.getAsJsonObject("error")
                if (errorObj.has("non_field_errors")) {
                    val nonFieldErrors = errorObj.getAsJsonArray("non_field_errors")
                    if (nonFieldErrors.size() > 0)
                        return nonFieldErrors[0].asString
                }

                // Si hay otros campos de error
                if (errorObj.has("detail"))
                    return errorObj.get("detail").asString
            }

            // 2. Estructura { "error": "mensaje" }
            if (jsonObject.has("error") && jsonObject.get("error").isJsonPrimitive) {
                return jsonObject.get("error").asString
            }

            // 3. Estructura { "non_field_errors": ["mensaje"] }
            if (jsonObject.has("non_field_errors")) {
                val nonFieldErrors = jsonObject.getAsJsonArray("non_field_errors")
                if (nonFieldErrors.size() > 0)
                    return nonFieldErrors[0].asString
            }

            // 4. Estructura { "detail": "mensaje" }
            if (jsonObject.has("detail"))
                return jsonObject.get("detail").asString

            // 5. Error de ValidationError de Django con array de errores
            if (jsonObject.has("0"))
                return jsonObject.get("0").asString

            "Error: $errorBody"
        } catch(e: Exception) {
            errorBody
        }
    }

    fun clearMessages() {
        mensaje.value = ""
        error.value = ""
    }
}