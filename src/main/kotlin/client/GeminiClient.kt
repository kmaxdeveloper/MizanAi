package client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GeminiClient(
    @Value("\${gemini.api.key}") private val apiKey: String
) {
    // Gemini API manzili (model sifatida 'gemini-1.5-flash' tavsiya qilinadi, juda tez va bepul)
    private val webClient = WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent")
        .build()

    private val objectMapper = jacksonObjectMapper()

    fun getAiAnalysis(prompt: String): AiResponse {
        val requestBody = mapOf(
            "contents" to listOf(
                mapOf("parts" to listOf(mapOf("text" to prompt)))
            )
        )

        val response = webClient.post()
            .uri { it.queryParam("key", apiKey).build() }
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        // Gemini javobini JSON-dan ajratib olish
        val candidates = response?.get("candidates") as List<*>
        val content = (candidates[0] as Map<*, *>)["content"] as Map<*, *>
        val parts = content["parts"] as List<*>
        val text = (parts[0] as Map<*, *>)["text"].toString()

        // JSON qismini ajratib olish (AI javobini tozalash)
        val cleanedJson = text.replace("```json", "").replace("```", "").trim()

        return objectMapper.readValue(cleanedJson, AiResponse::class.java)
    }
}

data class AiResponse(val score: Int, val feedback: String)