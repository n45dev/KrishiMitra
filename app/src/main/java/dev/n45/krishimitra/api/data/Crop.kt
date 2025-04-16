package dev.n45.krishimitra.api.data

data class Crop(
    val id: Int,
    val image: String?,
    val type: String,
    val tempRange: String,
    val phRange: String,
    val duration: Int,
    val title: String,
    val description: String,
    val soilType: String,
    val rainfallRequired: Boolean,
    val marketValue: Int
)