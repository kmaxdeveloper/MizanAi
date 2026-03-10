package service

import domain.AuditLog
import dto.AuditResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import parser.MizanParser
import repository.AuditLogRepository
import uz.mizanai.client.GeminiClient

@Service
class MizanAuditService(
    private val parser: MizanParser,
    private val repository: AuditLogRepository,
    private val aiClient: GeminiClient
) {

    fun process(file: MultipartFile, studentName: String, subject: String): AuditResponse {
        // 1. Faylni matnga aylantirish (PDF yoki DOCX)
        val text = parser.parse(file)

        // 2. AI uchun Prompt (kengaytirilgan)
        val prompt = """
            Fan: $subject
            Talabaning ishi: 
            ---
            $text
            ---
            Vazifa: Ushbu matnni akademik uslub, mavzuni yoritilishi va imlo xatolari bo'yicha tahlil qiling.
            Javobni quyidagi formatda qaytaring (faqat JSON):
            {"baho": 0-5, "izoh": "Qisqacha tanqidiy izoh"}
        """.trimIndent()

        // 3. AI-dan tahlilni olish
        val aiResult = aiClient.getAiAnalysis(prompt)

        // 4. Bazaga saqlash
        // Log yaratamiz
        val log = AuditLog(
            studentName = studentName,
            subjectName = subject,
            extractedText = text,
            score = aiResult.score,
            feedback = aiResult.feedback
        )

        // Bazaga saqlaymiz
        repository.save(log)

        // 5. Natijani qaytaramiz
        return AuditResponse(
            score = aiResult.score,
            feedback = aiResult.feedback
        )
    }
}