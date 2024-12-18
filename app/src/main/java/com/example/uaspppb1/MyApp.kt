package com.example.uaspppb1

import android.app.Application
import android.content.Context
import com.example.uaspppb1.DataBase.AppDatabase
import com.example.uaspppb1.Api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {
    companion object {
        const val PREFS_NAME = "user_prefs"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    private lateinit var database: AppDatabase
    lateinit var apiService: ApiService

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ppbo-api.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun getDatabase(): AppDatabase {
        return database
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}