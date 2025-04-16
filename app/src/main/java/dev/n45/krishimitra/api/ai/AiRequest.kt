package dev.n45.krishimitra.api.ai

data class AiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)