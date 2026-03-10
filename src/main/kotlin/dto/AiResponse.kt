package uz.mizanai.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AiResponse(
    @JsonProperty("baho") val score: Int,
    @JsonProperty("izoh") val feedback: String,
    val mistakes: List<String>? = emptyList() // Agar AI xatolarni ham bersa
)