package com.tomifas.TomiPay.data.model


data class ResetPasswordRequest(
    val phone: String,
    val otp: String,
    val new_password: String
)