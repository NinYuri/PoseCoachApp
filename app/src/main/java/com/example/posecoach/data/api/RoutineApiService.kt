package com.example.posecoach.data.api

import com.example.posecoach.data.responses.GenRoutineResponse
import com.example.posecoach.data.responses.RoutDaysResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RoutineApiService {
    @POST("routines/generate/")
    suspend fun generateRoutine(): Response<GenRoutineResponse>

    @GET("routines/active/")
    suspend fun getActiveRoutine(): Response<GenRoutineResponse>

    @GET("routines/{rutina_id}/days/")
    suspend fun getRoutineByDays(
        @Path("rutina_id") rutinaId: String
    ): Response<RoutDaysResponse>
}