package com.example.posecoach.data.model

data class RegistroUsuario (
    var temporal_id: Int = 0,
    var username: String = "",
    var sex: String = "",
    var date_birth: String = "",
    var height: Int = 0,
    var goal: String = "",
    var experience: String = "",
    var equipment: String = ""
)