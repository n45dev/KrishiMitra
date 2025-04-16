package dev.n45.krishimitra.api.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val text: String,
    val isSentByUser: Boolean,
    val time: String = SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Date(System.currentTimeMillis()))
)