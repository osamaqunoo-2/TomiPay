package com.tomifas.TomiPay.data.remote

import android.content.Context
import com.tomifas.TomiPay.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // no token
    private val noAuthClient = OkHttpClient.Builder().build()

    private val noAuthRetrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(noAuthClient)
        .build()

    val authApi: AuthApi = noAuthRetrofit.create(AuthApi::class.java)

    //  with token
    fun provideAuthenticatedApi(context: Context): AuthApi {
        val authClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        val authRetrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(authClient)
            .build()

        return authRetrofit.create(AuthApi::class.java)
    }
}