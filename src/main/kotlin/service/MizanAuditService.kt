package uz.mizanai.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import uz.mizanai.client.GeminiClient
import uz.mizanai.domain.AuditLog
import uz.mizanai.dto.AuditResponse
import uz.mizanai.parser.MizanParser
import uz.mizanai.repository.AuditLogRepository

@Service
class MizanAuditService(
    private val parser: MizanParser,
    private val repository: AuditLogRepository,
    private val aiClient: GeminiClient
) {

    fun process(file: MultipartFile, studentName: String, subject: String): AuditResponse {
        // 1. Faylni matnga aylantirish
        val text = try {
            parser.parse(file)
        } catch (e: Exception) {
            throw RuntimeException("Faylni o'qib bo'lmadi: ${e.message}")
        }

        // 2. Prompt (Sen yozgan formatga moslab)
        val prompt = """
            Fan: $subject
            Talabaning ishi: $text
            
            Ushbu ishni tekshiring va quyidagi JSON formatida javob bering:
            {"baho": 0-5, "izoh": "Izohingiz", "mistakes": ["xato 1", "xato 2"]}
        """.trimIndent()

        // 3. AI tahlili
        val aiResult = aiClient.getAiAnalysis(prompt)

        // 4. Bazaga saqlash
        val log = AuditLog(
            studentName = studentName,
            subjectName = subject,
            extractedText = text.take(2000), // Bazada joy tejash uchun
            score = aiResult.score,
            feedback = aiResult.feedback
        )
        repository.save(log)

        // 5. Natija
        return AuditResponse(
            score = aiResult.score,
            feedback = aiResult.feedback
        )
    }
}