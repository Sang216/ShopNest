package com.shopnest.productcatalog.service;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LlmMarketingNotifyService implements IMarketNotifierService {

    private final RestTemplate restTemplate;

    public LlmMarketingNotifyService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public String getMarketNotifyMessage() {
        ResponseEntity<Object> response =
                restTemplate.getForEntity("https://api.groq.com/openai/v1/chat/completions",
                        Object.class);

        return response.toString();
    }
}
