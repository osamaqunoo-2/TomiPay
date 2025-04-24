package com.tomifas.TomiPay.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.tomifas.TomiPay.data.remote.ApiClient
import com.tomifas.TomiPay.data.repositry.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository(ApiClient.authApi)

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _registerResponse = MutableStateFlow<Response<RegisterResponse>?>(null)
    val registerResponse: StateFlow<Response<RegisterResponse>?> = _registerResponse

    fun registerUser(firstName: String, lastName: String, phone: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.registerUser(
                    RegisterRequest(
                        first_name = firstName,
                        last_name = lastName,
                        phone = phone
                    )
                )
                _registerResponse.value = response
            } catch (e: Exception) {
                _registerResponse.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    private val _loginResponse = MutableStateFlow<Response<LoginResponse>?>(null)
    val loginResponse: StateFlow<Response<LoginResponse>?> = _loginResponse

    fun loginUser(context: Context, phone: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val apiWithToken = ApiClient.provideAuthenticatedApi(context)
                val repositoryWithToken = AuthRepository(apiWithToken)

                val response = repositoryWithToken.loginUser(LoginRequest(phone))
                _loginResponse.value = response
            } catch (e: Exception) {
                _loginResponse.value = null
            } finally {
                _loading.value = false
            }


        }
    }


    private val _verifyResponse = MutableStateFlow<Response<VerifyOtpResponse>?>(null)
    val verifyResponse: StateFlow<Response<VerifyOtpResponse>?> = _verifyResponse

    fun verifyOtp(phone: String, otp: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.verifyOtp(
                    VerifyOtpRequest(phone, otp)
                )
                _verifyResponse.value = response
            } catch (e: Exception) {
                _verifyResponse.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    private val _sendOtpResponse = MutableStateFlow<Response<SendOtpResponse>?>(null)
    val sendOtpResponse: StateFlow<Response<SendOtpResponse>?> = _sendOtpResponse

    fun sendOtpForLogin(phone: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.sendLoginOtp(SendOtpRequest(phone))
                _sendOtpResponse.value = response
            } catch (e: Exception) {
                _sendOtpResponse.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    private val _resetPasswordResponse = MutableStateFlow<Response<ResetPasswordResponse>?>(null)
    val resetPasswordResponse: StateFlow<Response<ResetPasswordResponse>?> = _resetPasswordResponse

    fun resetPassword(phone: String, otp: String, newPassword: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.resetPassword(
                    ResetPasswordRequest(
                        phone = phone,
                        otp = otp,
                        new_password = newPassword
                    )
                )
                _resetPasswordResponse.value = response
            } catch (e: Exception) {
                _resetPasswordResponse.value = null
            } finally {
                _loading.value = false
            }
        }
    }
}