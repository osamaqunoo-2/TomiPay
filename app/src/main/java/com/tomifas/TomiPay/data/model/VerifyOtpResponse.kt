package com.tomifas.TomiPay.data.model

data class VerifyOtpResponse(
    val token: String,
    val user: User
)