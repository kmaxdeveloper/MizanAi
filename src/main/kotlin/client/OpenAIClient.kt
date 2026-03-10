package uz.mizanai.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.beans.factory.annotation.Value
import uz.mizanai.dto.AiResponse

@Component
class OpenAIClient(
    @Value("\${openai.api.key}") private val apiKey: String
) {
    private val webClient = WebClient.builder()
        .baseUrl("https://api.openai.com/v1/chat/completions")
        .defaultHeader("Authorization", "Bearer $apiKey")
        .build()

    private val objectMapper = jacksonObjectMapper()

    fun getAiAnalysis(prompt: String): AiResponse {
        val requestBody = mapOf(
            "model" to "gpt-4o",
            "messages" to listOf(
                mapOf("role" to "system", "content" to "Siz auditorisiz. Javobni faqat JSON formatida qaytaring: {\"score\": Int, \"feedback\": String}"),
                mapOf("role" to "user", "content" to prompt)
            )
        )

        val response = webClient.post()
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        val choices = response?.get("choices") as List<*>
        val message = (choices[0] as Map<*, *>)["message"] as Map<*, *>
        val jsonString = message["content"].toString().replace("```json", "").replace("```", "").trim()

        return objectMapper.readValue(jsonString)
    }
}