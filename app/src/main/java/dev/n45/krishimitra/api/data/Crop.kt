package dev.n45.krishimitra.api.data

data class Crop(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val duration: String,
    val type: String,
    val soil_type: String,
    val temp_range: String,
    val is_rainfall_required: String,
    val ph_range: String,
    val market_value: Int
)