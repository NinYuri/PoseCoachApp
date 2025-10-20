package com.example.posecoach.data.responses

data class LoginResponse(
    val refresh_token: String? = null,
    val access_token: String? = null,
    val user: UserData? = null,
    val error: String? = null
)

data class UserData(
    val id: Int? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val is_active: Boolean? = null
)
