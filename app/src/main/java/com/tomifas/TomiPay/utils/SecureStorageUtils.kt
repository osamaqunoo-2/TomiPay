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
    fun clearUser(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().remove(USER_KEY).apply()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun logout(context: Context) {
//        val prefs = getPrefs(context)
//        prefs.edit().clear().apply()


        clearToken(context)
        clearUser(context)


    }

    private const val BIOMETRIC_ENABLED_KEY = "biometric_enabled"

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getEncryptedPrefs(context: Context) = EncryptedSharedPreferences.create(
        PREF_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @RequiresApi(Build.VERSION_CODES.M)
    fun enableBiometric(context: Context) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit().putBoolean(BIOMETRIC_ENABLED_KEY, true).apply()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isBiometricEnabled(context: Context): Boolean {
        val prefs = getEncryptedPrefs(context)
        return prefs.getBoolean(BIOMETRIC_ENABLED_KEY, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun disableBiometric(context: Context) {
        val prefs = getEncryptedPrefs(context)
        prefs.edit().putBoolean("biometric_enabled", false).apply()
    }

    private const val KEY_PHONE_NUMBER = "saved_phone_number"


    @RequiresApi(Build.VERSION_CODES.M)
    fun savePhoneNumber(context: Context, phoneNumber: String) {
        getEncryptedPrefs(context).edit().putString(KEY_PHONE_NUMBER, phoneNumber).apply()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getSavedPhoneNumber(context: Context): String? {
        return getEncryptedPrefs(context).getString(KEY_PHONE_NUMBER, null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun clearPhoneNumber(context: Context) {
        getEncryptedPrefs(context).edit().remove(KEY_PHONE_NUMBER).apply()
    }
}