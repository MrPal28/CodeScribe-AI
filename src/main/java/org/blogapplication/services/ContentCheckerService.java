package org.blogapplication.services;


import jakarta.annotation.PostConstruct;
import org.blogapplication.model.ContentCheckResponse;
import org.blogapplication.api.response.PromptRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ContentCheckerService {

    private final WebClient webClient;

    public ContentCheckerService() {
        this.webClient = WebClient.builder().baseUrl("http://127.0.0.1:5000").build();
    }

    public ContentCheckResponse sendPrompt(PromptRequest promptRequest) {
        if (webClient == null) {
            throw new IllegalStateException("WebClient not initialized. Check if initWebClient() was called.");
        }

        return webClient.post()
                .uri("/chat")
                .bodyValue(promptRequest)
                .retrieve()
                .bodyToMono(ContentCheckResponse.class)
                .onErrorResume(error -> Mono.just(getErrorResponse("AI API error: " + error.getMessage())))
                .block();
    }

    private ContentCheckResponse getErrorResponse(String message) {
        ContentCheckResponse error = new ContentCheckResponse();
        error.setInput("");
        error.setModeration_result(message);
        return error;
    }
}
