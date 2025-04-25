package dev.n45.krishimitra.api.data

data class News(
    val id: Int,
    val title: String,
    val short_description: String,
    val content: String,
    val image: String?,
    val date: String,
    val tags: String
)
