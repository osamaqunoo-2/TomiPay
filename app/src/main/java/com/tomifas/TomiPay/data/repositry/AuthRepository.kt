package com.tomifas.TomiPay.data.repositry

import com.tomifas.TomiPay.data.model.LoginRequest
import com.tomifas.TomiPay.data.model.LoginResponse
import com.tomifas.TomiPay.data.model.RegisterRequest
import com.tomifas.TomiPay.data.remote.AuthApi
import retrofit2.Response
import com.tomifas.TomiPay.data.model.RegisterResponse
import com.tomifas.TomiPay.data.model.ResetPasswordRequest
import com.tomifas.TomiPay.data.model.ResetPasswordResponse
import com.tomifas.TomiPay.data.model.SendOtpRequest
import com.tomifas.TomiPay.data.model.SendOtpResponse
import com.tomifas.TomiPay.data.model.VerifyOtpRequest
import com.tomifas.TomiPay.data.model.VerifyOtpResponse

class AuthRepository(private val api: AuthApi) {
    suspend fun registerUser(request: RegisterRequest): Response<RegisterResponse> {
        return api.register(request)
    }
    suspend fun loginUser(request: LoginRequest): Response<LoginResponse> {
        return api.login(request)
    }
    suspend fun verifyOtp(request: VerifyOtpRequest): Response<VerifyOtpResponse> {
        return api.verifyOtp(request)
    }

    suspend fun sendLoginOtp(request: SendOtpRequest): Response<SendOtpResponse> {
        return api.sendLoginOtp(request)
    }
    suspend fun resetPassword(request: ResetPasswordRequest): Response<ResetPasswordResponse> {
        return api.resetPassword(request)
    }
}