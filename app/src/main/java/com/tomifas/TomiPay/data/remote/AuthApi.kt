package com.tomifas.TomiPay.data.remote


import com.tomifas.TomiPay.data.model.LoginRequest
import com.tomifas.TomiPay.data.model.LoginResponse
import com.tomifas.TomiPay.data.model.RegisterRequest
import com.tomifas.TomiPay.data.model.RegisterResponse
import com.tomifas.TomiPay.data.model.ResetPasswordRequest
import com.tomifas.TomiPay.data.model.ResetPasswordResponse
import com.tomifas.TomiPay.data.model.SendOtpRequest
import com.tomifas.TomiPay.data.model.SendOtpResponse
import com.tomifas.TomiPay.data.model.VerifyOtpRequest
import com.tomifas.TomiPay.data.model.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("verify-otp")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("auth/login/send-otp")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun sendLoginOtp(
        @Body request: SendOtpRequest
    ): Response<SendOtpResponse>

    @POST("auth/reset-password")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResetPasswordResponse>
}