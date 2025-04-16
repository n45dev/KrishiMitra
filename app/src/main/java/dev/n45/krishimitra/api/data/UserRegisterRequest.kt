package dev.n45.krishimitra.api.data

data class UserRegisterRequest(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)
