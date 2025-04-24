package com.tomifas.TomiPay.data.remote


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.tomifas.TomiPay.utils.SecureStorageUtils
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SecureStorageUtils.getToken(context)
        val requestBuilder = chain.request().newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}