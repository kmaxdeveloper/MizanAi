package api

import dto.AuditResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import service.MizanAuditService

@RestController
@RequestMapping("/api/v1/mizan")
class MizanController(private val auditService: MizanAuditService) {

    @PostMapping("/audit")
    fun upload(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("studentName") studentName: String,
        @RequestParam("subject") subject: String
    ): ResponseEntity<AuditResponse> {
        val result = auditService.process(file, studentName, subject)
        return ResponseEntity.ok(result)
    }
}