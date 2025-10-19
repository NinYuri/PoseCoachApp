package com.example.posecoach.data.responses

data class OtpResponse(
    val mensaje: String? = null,
    val error: String? = null,
    val sugerencia: String? = null,
    val temporal_id: Int? = null
)
