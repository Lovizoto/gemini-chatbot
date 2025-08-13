package br.com.lovizoto.chatbot.service;


import br.com.lovizoto.chatbot.config.AppConfig;
import br.com.lovizoto.chatbot.model.Content;
import br.com.lovizoto.chatbot.model.GeminiRequest;
import br.com.lovizoto.chatbot.model.GenerationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class GeminiChatService {

    private static final Logger log = LoggerFactory.getLogger(GeminiChatService.class);
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String apiUrl;
    private String apiKey;

    public GeminiChatService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        this.objectMapper = new ObjectMapper();
        this.apiKey = AppConfig.getProperty("gemini.api.key");
        this.apiUrl = AppConfig.getProperty("gemini.api.url");
    }

    public Optional<String> generateContent(List<Content> history, GenerationConfig config) {
        if (apiKey == null || apiKey.isBlank() || "SUA_CHAVE_API_AQUI".equals(apiKey)) {
            log.error("A chave da API do Gemini não foi configurada em application.properties.");
            return Optional.of("ERRO: A API Key não foi configurada.");
        }

        try {
            GeminiRequest requestPayload = new GeminiRequest();
            requestPayload.setContents(history);
            requestPayload.setGenerationConfig(config);

            String requestBody = objectMapper.writeValueAsString(requestPayload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            log.debug("Enviando requisição para o Gemini: {}", requestBody);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.debug("Resposta recebida do Gemini: {}", response.body());

                return Optional.ofNullable(
                        objectMapper.readTree(response.body())
                                .path("candidates").get(0)
                                .path("content").path("parts").get(0)
                                .path("text").asText()
                );
            } else {
                log.error("Erro na API do Gemini ({}): {}", response.statusCode(), response.body());
                return Optional.empty();
            }

        } catch (Exception e) {
            log.error("Falha ao se comunicar com a API do Gemini.", e);
            return Optional.empty();
        }
    }

}
