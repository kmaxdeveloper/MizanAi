package uz.mizanai.parser

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class MizanParser {

    fun parse(file: MultipartFile): String {
        val filename = file.originalFilename?.lowercase() ?: ""

        return when {
            filename.endsWith(".pdf") -> parsePdf(file)
            filename.endsWith(".docx") -> parseDocx(file)
            else -> throw Exception("Faqat PDF yoki DOCX formatini yuklang!")
        }
    }

    private fun parsePdf(file: MultipartFile): String {
        PDDocument.load(file.inputStream).use { document ->
            return PDFTextStripper().getText(document)
        }
    }

    private fun parseDocx(file: MultipartFile): String {
        XWPFDocument(file.inputStream).use { document ->
            return XWPFWordExtractor(document).text
        }
    }
}