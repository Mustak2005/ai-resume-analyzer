
package com.mustak.resumeanalyzer.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OllamaService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String generateResponse(String prompt) {

        try {
            Map<String, Object> requestData = Map.of(
                    "model", "qwen3:4b",
                    "prompt", prompt,
                    "stream", false
            );

            String requestBody =
                    objectMapper.writeValueAsString(requestData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "http://localhost:11434/api/generate"
                    ))
                    .timeout(Duration.ofMinutes(5))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Ollama returned status: "
                                + response.statusCode()
                                + ". Details: " + response.body()
                );
            }

            JsonNode json = objectMapper.readTree(response.body());

            return json.path("response").asText();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not process the Ollama request: "
                            + e.getMessage(), e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Ollama request was interrupted.", e
            );
        }
    }
}

