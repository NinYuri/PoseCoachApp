package com.example.posecoach.data.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.model.LoginRequest
import com.example.posecoach.data.model.LogoutRequest
import com.example.posecoach.data.responses.LoginResponse
import com.example.posecoach.data.token.TokenManager
import com.example.posecoach.network.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel: ViewModel() {
    var loading = mutableStateOf(false)
    var mensaje = mutableStateOf("")
    var error = mutableStateOf("")

    var accessToken = mutableStateOf("")
    var refreshToken = mutableStateOf("")
    var userId = mutableStateOf(0)
    var username = mutableStateOf("")
    var email = mutableStateOf("")
    var phone = mutableStateOf("")
    var isLoggedIn = mutableStateOf(false)

    private val api = ApiClient.apiService

    fun Login(loginData: LoginRequest) {
        loading.value = true
        error.value = ""
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val response: Response<LoginResponse> = api.loginUser(loginData)

                if(response.isSuccessful) {
                    val body = response.body()

                    if(body != null) {
                        refreshToken.value = body.refresh_token ?: ""
                        accessToken.value = body.access_token ?: ""
                        userId.value = body.user?.id ?: 0
                        username.value = body.user?.username ?: ""
                        email.value = body.user?.email ?: ""
                        phone.value = body.user?.phone ?: ""

                        TokenManager.updateTokens(
                            access = body.access_token ?: "",
                            refresh = body.refresh_token ?: "",
                        )

                        mensaje.value = "Inicio de sesión exitoso."
                        isLoggedIn.value = true
                    } else {
                        error.value = "Lo siento, recibí una respuesta vacía del servidor."
                        isLoggedIn.value = false
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody) ?: "Lo siento, credenciales inválidas."
                    isLoggedIn.value = false
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
                isLoggedIn.value = false
            }
            loading.value = false
        }
    }

    fun Logout() {
        viewModelScope.launch {
            try {
                val refresh = refreshToken.value

                if(refresh.isEmpty()) {
                    error.value = "No hay token para cerrar sesión"
                    return@launch
                }

                val response = api.logoutUser(LogoutRequest(refresh) )
                if(response.isSuccessful) {
                    val body = response.body()
                    mensaje.value = body?.mensaje ?: "¡Nos vemos después!"

                    logout()
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = parseError(errorBody) ?: "Error al cerrar sesión"
                }
            } catch (e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
        }
    }

    private fun parseError(errorBody: String?): String? {
        if(errorBody.isNullOrEmpty()) return null
        val regex = """"error":\s*"([^"]+)"""".toRegex()
        return regex.find(errorBody)?.groupValues?.get(1)
    }

    fun logout() {
        accessToken.value = ""
        refreshToken.value = ""
        userId.value = 0
        username.value = ""
        email.value = ""
        phone.value = ""
        isLoggedIn.value = false
        TokenManager.clearTokens()
    }

    fun clearMessages() {
        mensaje.value = ""
        error.value = ""
    }
}