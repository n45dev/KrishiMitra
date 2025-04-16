package dev.n45.krishimitra.api.weather

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float,
        @Query("units") units: String = "metric"
    ): WeatherData
}