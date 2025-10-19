package com.example.posecoach.data.model

data class RegistroRequest(
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var confirm_password: String = "",
)
