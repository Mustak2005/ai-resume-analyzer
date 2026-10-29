package com.mustak.resumeanalyzer.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mustak.resumeanalyzer.service.OllamaService;

@RestController
public class AiTestController {

    private final OllamaService ollamaService;

    public AiTestController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @GetMapping("/api/ai-test")
    public Map<String, String> testAI() {

        String response = ollamaService.generateResponse(
                "Reply with only: AI connected successfully"
        );

        return Map.of("message", response);
    }
}