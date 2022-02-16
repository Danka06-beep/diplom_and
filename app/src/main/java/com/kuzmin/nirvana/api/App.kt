package com.kuzmin.nirvana.api

import android.app.Application
import android.util.Log
import com.kuzmin.nirvana.API_SHARED_FILE
import com.kuzmin.nirvana.AUTHENTICATED_SHARED_KEY
import com.kuzmin.nirvana.BASE_URL
import com.kuzmin.nirvana.other.InjectAuthTokenInterceptor
import com.kuzmin.nirvana.repository.NetworkRepository
import com.kuzmin.nirvana.repository.Repository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        lateinit var repository: Repository
            private set
    }
    override fun onCreate() {
        super.onCreate()

        val httpLoggerInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            Log.d("MyLog", "okhttp: $message")
        })

        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor {
                getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE).getString(
                    AUTHENTICATED_SHARED_KEY, null
                )
            })
            .addInterceptor(httpLoggerInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("$BASE_URL/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)

        repository = NetworkRepository(api)



    }
}