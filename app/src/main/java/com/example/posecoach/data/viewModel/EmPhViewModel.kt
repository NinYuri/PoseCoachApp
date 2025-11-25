package com.example.posecoach.data.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.model.NewEmailRequest
import com.example.posecoach.data.model.NewPhoneRequest
import com.example.posecoach.data.model.ProfileOTP
import com.example.posecoach.data.model.ResendOtp
import com.example.posecoach.data.responses.ResetPassResponse
import com.example.posecoach.network.ApiClient
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import retrofit2.Response

class EmPhViewModel: ViewModel() {
    var loading = mutableStateOf(false)
    var mensaje = mutableStateOf("")
    var error = mutableStateOf("")

    // Datos guardados
    var userEmail = mutableStateOf("")
    var userPhone = mutableStateOf("")

    private val api = ApiClient.apiService

    fun addEmail(email: String) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = NewEmailRequest(email = email)
                val response: Response<ResetPassResponse> = api.addEmail(request)

                handleResponse(response)
                userEmail.value = email
            } catch (e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun changeEmail(email: String) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = NewEmailRequest(email = email)
                val response: Response<ResetPassResponse> = api.changeEmail(request)

                handleResponse(response)
                userEmail.value = email
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun verifyEmailOTP(otp: String) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = ProfileOTP(otp = otp)
                val response: Response<ResetPassResponse> = api.verifyEmOTP(request)

                handleResponse(response)
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun addPhone(phone: String) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = NewPhoneRequest(phone = phone)
                val response: Response<ResetPassResponse> = api.addPhone(request)

                handleResponse(response)
                userPhone.value = phone
            } catch (e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun changePhone(phone: String) {
        loading.value
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = NewPhoneRequest(phone = phone)
                val response: Response<ResetPassResponse> = api.changePhone(request)

                handleResponse(response)
                userPhone.value = phone
            } catch (e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun verifyPhoneOTP(otp: String) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = ProfileOTP(otp = otp)
                val response: Response<ResetPassResponse> = api.verifyPhOTP(request)

                handleResponse(response)
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun resendProfileOTP(resendData: ResendOtp) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val response: Response<ResetPassResponse> = api.resendProfileOTP(resendData)
                handleResponse(response)
            } catch (e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    private fun handleResponse(response: Response<ResetPassResponse>) {
        if(response.isSuccessful) {
            val body = response.body()

            if(body != null) {
                if(body.mensaje != null) {
                    mensaje.value = body.mensaje
                    error.value = ""
                } else if(body.error != null) {
                    error.value = body.error
                    mensaje.value = ""
                } else {
                    error.value = "Respuesta inesperada del servidor."
                }
            } else {
                error.value = "Lo siento, recibí una respuesta vacía del servidor."
            }
        } else {
            val errorBody = response.errorBody()?.string()
            error.value = parseError(errorBody) ?: "Error desconocido. Por favor, inténtelo de nuevo."
        }
    }

    private fun parseError(errorBody: String?): String {
        if (errorBody.isNullOrEmpty())
            return "Lo siento, hubo un error desconocido."

        return try {
            val jsonElement = JsonParser().parse(errorBody)
            val jsonObject = jsonElement.asJsonObject

            // 2. Estructura { "error": "mensaje" }
            if (jsonObject.has("error") && jsonObject.get("error").isJsonPrimitive) {
                return jsonObject.get("error").asString
            }

            "Error: $errorBody"
        } catch(e: Exception) {
            errorBody
        }
    }

    fun clearMessages() {
        mensaje.value = ""
        error.value = ""
    }

    fun clearFields() {
        userEmail.value = ""
        userPhone.value = ""
    }
}