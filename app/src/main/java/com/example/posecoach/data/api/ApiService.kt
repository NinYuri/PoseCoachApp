package com.example.posecoach.data.api

import com.example.posecoach.data.model.CheckUsername
import com.example.posecoach.data.model.LoginRequest
import com.example.posecoach.data.model.RegistroRequest
import com.example.posecoach.data.model.RegistroUsuario
import com.example.posecoach.data.model.ResendOtp
import com.example.posecoach.data.model.VerifyOTP
import com.example.posecoach.data.responses.CompleteResponse
import com.example.posecoach.data.responses.LoginResponse
import com.example.posecoach.data.responses.OtpResponse
import com.example.posecoach.data.responses.RegisterResponse
import com.example.posecoach.data.responses.ResendOtpResponse
import com.example.posecoach.data.responses.UsernameResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/register/initial/")
    suspend fun registerUser(
        @Body register: RegistroRequest
    ): Response<RegisterResponse>

    @POST("users/register/otp/")
    suspend fun verifyOtp(
        @Body otp: VerifyOTP
    ): Response<OtpResponse>

    @POST("users/register/otpresend/")
    suspend fun resendOtp(
        @Body resend: ResendOtp
    ): Response<ResendOtpResponse>

    @POST("users/register/username/")
    suspend fun verifyUsername(
        @Body username: CheckUsername
    ): Response<UsernameResponse>

    @POST("users/register/complete/")
    suspend fun completeUser(
        @Body user: RegistroUsuario
    ): Response<CompleteResponse>

    @POST("users/login/")
    suspend fun loginUser(
        @Body login: LoginRequest
    ): Response<LoginResponse>
}