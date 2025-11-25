package com.example.posecoach.data.responses

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val user: ProfileData
)

data class ProfileData(
    val email: String?,
    val phone: String?,
    val username: String,
    @SerializedName("date_birth") val dateBirth: String,
    val sex: String,
    val height: Int,
    val goal: String,
    val experience: String,
    val equipment: String
)