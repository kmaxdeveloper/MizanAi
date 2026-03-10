package uz.mizanai.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uz.mizanai.dto.AiResponse // DTO papkangni importini to'g'irlab qo'y

@Component
class GeminiClient(
    @Value("\${gemini.api.key}") private val apiKey: String
) {
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

        // WebClient so'rovi
        val response = webClient.post()
            .uri { it.queryParam("key", apiKey).build() }
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map::class.java)
            .block() ?: throw Exception("AI-dan javob kelmadi!")

        // 1. Logikani xavfsizroq qilish
        val candidates = (response["candidates"] as? List<*>)?.firstOrNull() as? Map<*, *>
        val content = candidates?.get("content") as? Map<*, *>
        val parts = content?.get("parts") as? List<*>
        val text = (parts?.firstOrNull() as? Map<*, *>)?.get("text")?.toString()
            ?: throw Exception("AI javobida format xatosi!")

        // 2. JSON-ni tozalash (Regex orqali har qanday markdown-ni olib tashlaydi)
        val cleanedJson = text.replace(Regex("```json|```", RegexOption.IGNORE_CASE), "").trim()

        // 3. ObjectMapper bilan DTO ga o'girish
        return try {
            objectMapper.readValue<AiResponse>(cleanedJson)
        } catch (e: Exception) {
            throw Exception("AI javobini JSON-ga o'girib bo'lmadi: $cleanedJson")
        }
    }
}