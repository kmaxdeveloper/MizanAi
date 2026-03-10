package uz.mizanai.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uz.mizanai.domain.AuditLog

@Repository
interface AuditLogRepository : JpaRepository<AuditLog, Long> {
    // JpaRepository o‘zi CRUD (create, read, update, delete)
    // amallarini ichiga oladi, shuning uchun boshqa hech narsa yozish shart emas.
}