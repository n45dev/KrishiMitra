package dev.n45.krishimitra.api.data

data class HistoryItem(
    val id: Int,
    val productId: String,
    val quantity: Int,
    val date: String,
    val mType: String,
)
