package org.blogapplication.scheduling;

// TODO: this class will create for testing when it is full fetch host then this class will be deleted


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class AIapiConnection {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.api}")
    private String API_URL;


    @Scheduled(cron = "0 */10 * * * *")
    public void callApiEveryTenMinutes() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            log.info("API response: {}", response);
        } catch (Exception e) {
            log.error("API response error: {}", e.getMessage());
        }
    }
}
