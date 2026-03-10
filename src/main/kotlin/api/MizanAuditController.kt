package uz.mizanai.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import uz.mizanai.dto.AuditResponse
import uz.mizanai.service.MizanAuditService

@RestController
@RequestMapping("/api/audit")
class MizanAuditController(
    private val auditService: MizanAuditService
) {

    @PostMapping("/check")
    fun checkHomework(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("studentName") studentName: String,
        @RequestParam("subject") subject: String
    ): ResponseEntity<AuditResponse> {

        // Servis orqali ishni bajaramiz
        val result = auditService.process(file, studentName, subject)

        // Natijani qaytaramiz
        return ResponseEntity.ok(result)
    }
}