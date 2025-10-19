package com.example.posecoach.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posecoach.data.model.RegistroRequest
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    fun registrarUsuario(datos: RegistroRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("Backend", "Datos enviados: $datos")
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}