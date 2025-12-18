package com.example.posecoach.data.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.mediaPipe.Exercise
import com.example.posecoach.data.responses.Ejercicio
import com.example.posecoach.data.responses.GenRoutineResponse
import com.example.posecoach.data.responses.RoutDaysResponse
import com.example.posecoach.network.ApiClientRoutines
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class RoutineViewModel: ViewModel() {
    var loading = mutableStateOf(false)
    var message = mutableStateOf("")
    var error = mutableStateOf("")

    // Variables
    var rutina_id = mutableStateOf("")
    val rutinaCompleta = MutableStateFlow<RoutDaysResponse?>(null)

    private val _selectedExercise = MutableStateFlow<Ejercicio?>(null)
    val selectedExercise = _selectedExercise

    private val api = ApiClientRoutines.routineApiService

    fun createRoutine() {
        loading.value = true
        clearStates()

        viewModelScope.launch {
            try {
                val response: Response<GenRoutineResponse> = api.generateRoutine()

                if(response.isSuccessful) {
                    val body = response.body()

                    if(body != null) {
                        message.value = body.message ?: "Rutina generada"
                        rutina_id.value = body.rutina_id ?: ""

                        getRoutine(body.rutina_id ?: "")
                    } else
                        error.value = "Lo siento, recibí una respuesta vacía del servidor."
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = Errores(errorBody) ?: "Error desconocido"
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun checkRoutine() {
        loading.value = true
        clearStates()

        viewModelScope.launch {
            try {
                val response: Response<GenRoutineResponse> = api.getActiveRoutine()

                if(response.isSuccessful) {
                    val body = response.body()

                    if(body != null && !body.rutina_id.isNullOrEmpty()) {
                        rutina_id.value = body.rutina_id
                        getRoutine(body.rutina_id)
                    }
                    else
                        createRoutine()
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = Errores(errorBody) ?: "Error desconocido"
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun getRoutine(rutinaId: String) {
        loading.value = true
        clearMessages()

        viewModelScope.launch {
            try {
                val response: Response<RoutDaysResponse> = api.getRoutineByDays(rutinaId)

                if(response.isSuccessful) {
                    val body = response.body()

                    if(body != null)
                        rutinaCompleta.value = body
                    else
                        error.value = "No se pudo obtener la rutina completa."
                } else {
                    val errorBody = response.errorBody()?.string()
                    error.value = Errores(errorBody) ?: "Error desconocido"
                }
            } catch(e: Exception) {
                error.value = e.message ?: "Error de conexión"
            }
            loading.value = false
        }
    }

    fun setSelectedExercise(ejercicio: Ejercicio) {
        _selectedExercise.value = ejercicio
    }

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

    fun clearMessages() {
        message.value = ""
        error.value = ""
    }

    fun clearStates() {
        error.value = ""
        message.value = ""
    }
}