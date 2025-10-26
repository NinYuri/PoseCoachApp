package com.example.posecoach.data.model

data class ResetPassEmailRequest(
    var email: String = "",
    var otp: String = "",
    var new_password: String = "",
    var new_password_confirm: String = ""
)

data class ResetPassPhoneRequest(
    var phone: String = "",
    var otp: String = "",
    var new_password: String = "",
    var new_password_confirm: String = ""
)
