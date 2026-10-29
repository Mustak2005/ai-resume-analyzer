package com.mustak.resumeanalyzer.controller;

import com.mustak.resumeanalyzer.entity.Analysis;
import com.mustak.resumeanalyzer.service.AnalysisService;
import com.mustak.resumeanalyzer.service.PdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final PdfService pdfService;
    private final AnalysisService analysisService;

    public ResumeController(PdfService pdfService, AnalysisService analysisService) {
        this.pdfService = pdfService;
        this.analysisService = analysisService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            String text = pdfService.extractText(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "filename", file.getOriginalFilename() == null ? "" : file.getOriginalFilename(),
                    "extractedText", text
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage() == null ? "Could not process PDF." : e.getMessage()
            ));
        }
    }

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "resumeText", required = false) String resumeText,
            @RequestParam("jobDescription") String jobDescription) {
        if ((resumeText == null || resumeText.isBlank()) || jobDescription == null || jobDescription.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Resume text and job description are required."
            ));
        }
        Analysis result = analysisService.analyze(resumeText, jobDescription, fileName);
        return ResponseEntity.ok(result);
    }
}
