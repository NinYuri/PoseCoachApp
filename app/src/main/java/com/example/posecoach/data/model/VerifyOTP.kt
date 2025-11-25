package com.example.posecoach.data.model

data class VerifyOTP(
    val temporal_id: Int = 0,
    val otp: String = ""
)

data class ProfileOTP(
    val otp: String = ""
)
