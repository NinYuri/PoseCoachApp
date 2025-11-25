package com.example.posecoach.data.repository

import com.example.posecoach.data.api.ApiService
import com.example.posecoach.data.model.UpdateRequest
import com.example.posecoach.data.responses.ProfileResponse
import retrofit2.Response

class ProfileRepo(private val api: ApiService) {
    suspend fun updateProfile(request: UpdateRequest): Response<ProfileResponse> {
        val updateMap = mutableMapOf<String, Any?>()

        request.username?.let { updateMap["username"] = it }
        request.sex?.let { updateMap["sex"] = it }
        request.date_birth?.let { updateMap["date_birth"] = it }
        request.height?.let { updateMap["height"] = it }
        request.goal?.let { updateMap["goal"] = it }
        request.experience?.let { updateMap["experience"] = it }
        request.equipment?.let { updateMap["equipment"] = it }

        return api.updateProfile(updateMap)
    }
}