package com.example.posecoach.data.responses

data class ProfileResponse(
    val mensaje: String? = null,
    val error: Map<String, List<String>>? = null
)
