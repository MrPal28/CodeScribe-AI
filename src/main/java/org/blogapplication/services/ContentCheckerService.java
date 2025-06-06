package org.blogapplication.services;


import org.blogapplication.cache.AppCache;
import org.blogapplication.model.ContentCheckResponse;
import org.blogapplication.api.response.PromptRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ContentCheckerService {

    private WebClient webClient;
    private final AppCache appCache;


    public ContentCheckerService(AppCache appCache) {
        this.appCache = appCache;
        initWebClient();
    }

    public void initWebClient() {
        String URL = appCache.cache.get(AppCache.key.AI_API.toString());
        this.webClient = WebClient.builder().baseUrl(URL).build();
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
