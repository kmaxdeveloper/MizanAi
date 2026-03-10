package exeption

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import uz.mizanai.dto.ErrorResponse

@ControllerAdvice
class GlobalExceptionHandler {

    // Fayl formati yoki boshqa mantiqiy xatolar uchun
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(e: Exception): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = e.message ?: "Noma'lum xatolik yuz berdi!",
            status = HttpStatus.BAD_REQUEST.value()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}