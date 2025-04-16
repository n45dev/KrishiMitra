package dev.n45.krishimitra.api.ai

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: AiRequest
    ): AiResponse
}