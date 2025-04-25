package dev.n45.krishimitra.api

import dev.n45.krishimitra.api.data.Crop
import dev.n45.krishimitra.api.data.HistoryItem
import dev.n45.krishimitra.api.data.LoginRequest
import dev.n45.krishimitra.api.data.News
import dev.n45.krishimitra.api.data.Product
import dev.n45.krishimitra.api.data.TokenResponse
import dev.n45.krishimitra.api.data.User
import dev.n45.krishimitra.api.data.UserRegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

const val basePath = "/api/v0.1"

interface ApiService {
    @POST("register")
    suspend fun registerUser(
        @Body
        userRegisterRequest: UserRegisterRequest
    ): User

    @POST("login")
    suspend fun loginUser(
        @Body
        loginRequest: LoginRequest
    ): TokenResponse

    @GET("${basePath}/users/me")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ): User

    @GET("${basePath}/products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
    ): List<Product>

    @GET("${basePath}/history")
    suspend fun getHistory(
        @Header("Authorization") token: String,
    ): List<HistoryItem>

    @GET("${basePath}/news")
    suspend fun getNews(
        @Header("Authorization") token: String,
    ): List<News>

    @GET("${basePath}/crops")
    suspend fun getCrops(
        @Header("Authorization") token: String,
    ): List<Crop>

    @POST("${basePath}/sell_products")
    suspend fun sellProducts(
        @Header("Authorization") token: String,
    )
}