package com.example.posecoach.data.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.posecoach.data.model.RegistroUsuario

class RegistroViewModel : ViewModel() {
    var usuario = mutableStateOf(RegistroUsuario())

    // Fecha de Nacimiento
    fun updateBirthday(year: Int, month: Int, day: Int) {
        val formattedDate = "%04d-%02d-%02d".format(year, month + 1, day)
        usuario.value = usuario.value.copy(date_birth = formattedDate)
    }
}