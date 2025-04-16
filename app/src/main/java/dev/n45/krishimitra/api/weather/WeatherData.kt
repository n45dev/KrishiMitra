package dev.n45.krishimitra.api.weather

data class WeatherData(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val sys: Sys
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int,
)

data class Weather(
    val icon: String,
    val main: String,
    val description: String
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Sys(
    val sunrise: Long,
    val sunset: Long
)