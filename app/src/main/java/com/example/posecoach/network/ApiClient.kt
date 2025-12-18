package com.example.posecoach.network

import com.example.posecoach.data.api.ApiService
import com.example.posecoach.data.token.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(160, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_USERS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}