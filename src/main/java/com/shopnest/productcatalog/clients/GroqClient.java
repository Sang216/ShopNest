package com.shopnest.productcatalog.clients;

import com.shopnest.productcatalog.dto.GroqPitchResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class GroqClient {

    private final RestClient restClient;
    private final String apiKey;

    public GroqClient(RestClient.Builder builder,
                      @Value("${groq.url}") String groqUrl,
                      @Value("${groq.api-key}") String apiKey) {
        this.restClient = builder.baseUrl(groqUrl)
                .defaultHeaders(headers -> headers.setBearerAuth(apiKey))
                .build();
        this.apiKey = apiKey;
    }

    @CircuitBreaker(name = "groq")
    public GroqPitchResponseDto getPitch(Map<String, Object> requestBody) {
        return restClient.post()
                .body(requestBody)
                .retrieve()
                .body(GroqPitchResponseDto.class);
    }
}
