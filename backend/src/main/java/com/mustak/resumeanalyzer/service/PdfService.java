
package com.mustak.resumeanalyzer.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfService {

    public String extractText(MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF file is empty.");
        }

        String name = file.getOriginalFilename();

        if (name == null || !name.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are supported.");
        }

        try (var document = Loader.loadPDF(file.getBytes())) {
            return new PDFTextStripper().getText(document).trim();
        }
    }
}

