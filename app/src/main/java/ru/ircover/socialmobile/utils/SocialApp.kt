package ru.ircover.socialmobile.utils

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.ircover.socialmobile.api.Api
import java.util.concurrent.TimeUnit

class SocialApp : Application() {
    companion object {//TODO: переделать на нормальный DI
        lateinit var gson: Gson
            private set
        lateinit var okHttpClient: OkHttpClient
            private set
        lateinit var retrofit: Retrofit
            private set
        lateinit var api: Api
            private set
        lateinit var userSessionWorker: UserSessionWorker
            private set
    }
    override fun onCreate() {
        super.onCreate()
        gson = GsonBuilder()
            .setLenient()
            .create()
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("http://hackathon.rodial.pro:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        api = retrofit.create(Api::class.java)
        userSessionWorker = UserSessionWorkerImpl()
    }
}