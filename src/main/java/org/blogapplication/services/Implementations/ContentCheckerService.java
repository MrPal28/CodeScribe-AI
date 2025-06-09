package org.blogapplication.services.Implementations;


import lombok.RequiredArgsConstructor;
import org.blogapplication.cache.AppCache;
import org.blogapplication.model.ContentCheckResponse;
import org.blogapplication.api.response.PromptRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ContentCheckerService {

    private final RestTemplate restTemplate;
    private final AppCache appCache;


    protected ContentCheckResponse sendPrompt(PromptRequest promptRequest) throws RuntimeException {
        String API = appCache.cache.get(AppCache.key.AI_API.toString());
        HttpEntity<PromptRequest> request = new HttpEntity<>(promptRequest);

        return restTemplate.exchange(API, HttpMethod.POST, request, ContentCheckResponse.class).getBody();
    }
}
