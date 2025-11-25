package com.example.posecoach.data.model

data class UpdateRequest(
    var username: String? = null,
    var sex: String? = null,
    var date_birth: String? = null,
    var height: Int? = null,
    var goal: String? = null,
    var experience: String? = null,
    var equipment: String? = null
)