package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.clients.GroqClient;
import com.shopnest.productcatalog.dto.GroqPitchResponseDto;
import com.shopnest.productcatalog.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PitchNotifyService {

    private static final String MODEL = "openai/gpt-oss-20b";
    private static final String SYSTEM_PROMPT = "You are a marketing copywriter for ShopNest, an online store. \"\n" +
            "            + \"You write short push notifications that make people want to open the app.";

    private final GroqClient groqClient;

    public PitchNotifyService(GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    public String getPitchNotifyMessage(Product product) {
        try {
            Map<String, Object> body = Map.of(
                    "model", MODEL,
                    "reasoning_effort", "low",
                    "max_completion_tokens", 300,
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", buildPrompt(product)))
            );

            GroqPitchResponseDto response = groqClient.getPitch(body);


            return response.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.warn("Error: ", e.getMessage());
            return "New arrival: " + product.getName() +
                    " for just $" + product.getPrice() + ". Check it out!";
        }
    }

    private String buildPrompt(Product product) {
        return """
                Write ONE push notification for this product.
                Rules:
                - Maximum 25 words.
                - Mention the price.
                - At most one emoji.
                - No hashtags.
                - Reply with only the notification text.

                Product: %s
                Category: %s
                Price: $%s
                Details: %s""".formatted(product.getName(), product.getCategory().getName(),
                product.getPrice(), product.getDescription());
    }
}
