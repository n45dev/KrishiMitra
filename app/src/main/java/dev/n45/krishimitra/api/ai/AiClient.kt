package dev.n45.krishimitra.api.ai

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    val aiService: AiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AiService::class.java)
    }
}