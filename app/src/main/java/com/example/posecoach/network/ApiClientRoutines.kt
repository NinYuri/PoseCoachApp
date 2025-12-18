package com.example.posecoach.network

import com.example.posecoach.data.api.RoutineApiService
import com.example.posecoach.data.token.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClientRoutines {
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(160, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val routineApiService: RoutineApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_ROUTINES)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RoutineApiService::class.java)
    }
}