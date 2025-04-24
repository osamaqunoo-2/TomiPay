package com.tomifas.TomiPay.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.tomifas.TomiPay.data.model.User

object SecureStorageUtils {

    private const val PREF_NAME = "secure_prefs"
    private const val TOKEN_KEY = "jwt_token"

    @RequiresApi(Build.VERSION_CODES.M)
    fun getPrefs(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun saveToken(context: Context, token: String) {
        val prefs = getPrefs(context)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getToken(context: Context): String? {
        val prefs = getPrefs(context)
        return prefs.getString(TOKEN_KEY, null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun clearToken(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().remove(TOKEN_KEY).apply()
    }



    private const val USER_KEY = "user_data"

    @RequiresApi(Build.VERSION_CODES.M)
    fun saveUser(context: Context, user: User) {
        val prefs = getPrefs(context)
        val json = Gson().toJson(user)
        prefs.edit().putString(USER_KEY, json).apply()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getUser(context: Context): User? {
        val prefs = getPrefs(context)
        val json = prefs.getString(USER_KEY, null) ?: return null
        return Gson().fromJson(json, User::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun logout(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().clear().apply()
    }
}