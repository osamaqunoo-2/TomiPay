package com.tomifas.TomiPay.data.model

data class VerifyOtpRequest(
    val phone: String,
    val otp: String
)