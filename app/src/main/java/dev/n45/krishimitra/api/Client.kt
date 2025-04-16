package dev.n45.krishimitra.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
//    private const val BASE_URL = "http://10.0.2.2:8000"
    private const val BASE_URL = "http://100.100.1.2:8000"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}