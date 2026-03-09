package parser

import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class MizanParser {

    fun parse(file: MultipartFile): String {
        val filename = file.originalFilename?.lowercase() ?: ""

        return when {
            filename.endsWith(".docx") -> extractFromDocx(file)
            filename.endsWith(".pdf") -> extractFromPdf(file)
            else -> throw IllegalArgumentException("Fayl formati noto'g'ri! Faqat .docx yoki .pdf qabul qilinadi.")
        }
    }

    private fun extractFromDocx(file: MultipartFile): String {
        return XWPFDocument(file.inputStream).use { doc ->
            XWPFWordExtractor(doc).text
        }
    }

    private fun extractFromPdf(file: MultipartFile): String {
        return PDDocument.load(file.inputStream).use { doc ->
            PDFTextStripper().getText(doc)
        }
    }
}