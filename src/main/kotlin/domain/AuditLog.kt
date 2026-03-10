package uz.mizanai.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "audit_log")
class AuditLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val studentName: String,

    @Column(nullable = false)
    val subjectName: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val extractedText: String,

    @Column(nullable = false)
    val score: Int,

    @Column(columnDefinition = "TEXT")
    val feedback: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)