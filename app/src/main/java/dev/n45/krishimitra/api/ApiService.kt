package dev.n45.krishimitra.api

import dev.n45.krishimitra.api.data.Crop
import dev.n45.krishimitra.api.data.HistoryItem
import dev.n45.krishimitra.api.data.News
import dev.n45.krishimitra.api.data.Product
import dev.n45.krishimitra.api.data.TokenResponse
import dev.n45.krishimitra.api.data.User
import dev.n45.krishimitra.api.data.UserRegisterRequest
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): UserRegisterRequest

    @FormUrlEncoded
    @POST("token")
    suspend fun getToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded"
    ): TokenResponse

    @GET("users/me")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ): User

    @GET("products")
    suspend fun getProducts(
        @Header("Authorization") token: String,
    ): List<Product>

    @GET("history")
    suspend fun getHistory(
        @Header("Authorization") token: String,
    ): List<HistoryItem>

    @GET("news")
    suspend fun getNews(
        @Header("Authorization") token: String,
    ): List<News>

    @GET("products/{id}")
    suspend fun getProductById(id: Int): Product

    @GET("crops")
    suspend fun getCrops(
        @Header("Authorization") token: String,
    ): List<Crop>

    @GET("crops/search")
    suspend fun searchCrops(
        @Header("Authorization")
        token: String,
        @Query("title")
        title: String
    ): List<Crop>

    @GET("crops/{id}")
    suspend fun getCropById(id: Int): Crop
}