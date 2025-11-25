package com.example.posecoach.data.api

import com.example.posecoach.data.model.CheckUsername
import com.example.posecoach.data.model.ForgotPassEmailRequest
import com.example.posecoach.data.model.ForgotPassPhoneRequest
import com.example.posecoach.data.model.LoginRequest
import com.example.posecoach.data.model.LogoutRequest
import com.example.posecoach.data.model.NewEmailRequest
import com.example.posecoach.data.model.NewPhoneRequest
import com.example.posecoach.data.model.ProfileOTP
import com.example.posecoach.data.model.RegistroRequest
import com.example.posecoach.data.model.RegistroUsuario
import com.example.posecoach.data.model.ResendOtp
import com.example.posecoach.data.model.ResetPassEmailRequest
import com.example.posecoach.data.model.ResetPassPhoneRequest
import com.example.posecoach.data.model.VerifyOTP
import com.example.posecoach.data.responses.CompleteResponse
import com.example.posecoach.data.responses.ForgotPassResponse
import com.example.posecoach.data.responses.LoginResponse
import com.example.posecoach.data.responses.LogoutResponse
import com.example.posecoach.data.responses.OtpResponse
import com.example.posecoach.data.responses.ProfileResponse
import com.example.posecoach.data.responses.RegisterResponse
import com.example.posecoach.data.responses.ResendOtpResponse
import com.example.posecoach.data.responses.ResetPassResponse
import com.example.posecoach.data.responses.UserProfileResponse
import com.example.posecoach.data.responses.UsernameResponse
import com.google.gson.stream.JsonToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {
    // USUARIOS
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

    @POST("/users/logout/")
    suspend fun logoutUser(
        @Body request: LogoutRequest
    ): Response<LogoutResponse>

    @POST("users/forgot/")
    suspend fun forgotPassEmail(
        @Body request: ForgotPassEmailRequest
    ): Response<ForgotPassResponse>

    @POST("users/forgot/")
    suspend fun forgotPassPhone(
        @Body request: ForgotPassPhoneRequest
    ): Response<ForgotPassResponse>

    @POST("users/password/otp/")
    suspend fun resendOTPass(
        @Body request: ResendOtp
    ): Response<ResendOtpResponse>

    @POST("users/reset/")
    suspend fun resetPassEmail(
        @Body request: ResetPassEmailRequest
    ): Response<ResetPassResponse>

    @POST("users/reset/")
    suspend fun resetPassPhone(
        @Body request: ResetPassPhoneRequest
    ): Response<ResetPassResponse>

    @GET("users/profile/")
    suspend fun getUserProfile(): Response<UserProfileResponse>

    @PATCH("users/profile/update/")
    suspend fun updateProfile(
        @Body request: Map<String, @JvmSuppressWildcards Any?>
    ): Response<ProfileResponse>

    @POST("users/profile/add/email/")
    suspend fun addEmail(
        @Body request: NewEmailRequest
    ): Response<ResetPassResponse>

    @POST("users/profile/change/email/")
    suspend fun changeEmail(
        @Body request: NewEmailRequest
    ): Response<ResetPassResponse>

    @POST("users/profile/email/otp/")
    suspend fun verifyEmOTP(
        @Body request: ProfileOTP
    ): Response<ResetPassResponse>

    @POST("users/profile/add/phone/")
    suspend fun addPhone(
        @Body request: NewPhoneRequest
    ): Response<ResetPassResponse>

    @POST("users/profile/change/phone/")
    suspend fun changePhone(
        @Body request: NewPhoneRequest
    ): Response<ResetPassResponse>

    @POST("users/profile/phone/otp/")
    suspend fun verifyPhOTP(
        @Body request: ProfileOTP
    ): Response<ResetPassResponse>

    @POST("users/profile/otp/resend/")
    suspend fun resendProfileOTP(
        @Body request: ResendOtp
    ): Response<ResetPassResponse>

    @DELETE("users/delete/")
    suspend fun deleteUser(): Response<ProfileResponse>
}