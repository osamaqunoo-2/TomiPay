package com.tomifas.TomiPay.data.model

import java.io.Serializable


data class User(
    val id: Int,
    val first_name: String,
    val last_name: String? = null,
    val phone: String
) : Serializable