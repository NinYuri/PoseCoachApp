package com.example.posecoach.data.responses

data class ForgotPassResponse(
    val mensaje: String? = null,
    val otp: String? = null,
    val error: String? = null
)
