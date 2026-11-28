package com.shopnest.productcatalog.clients;

import com.shopnest.productcatalog.dto.GeminiEmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class EmbeddingClient {
    private static final String MODEL = "gemini-embedding-001";

    private final RestClient restClient;

    public EmbeddingClient(RestClient.Builder builder,
                           @Value("${embedding.url:https://generativelanguage.googleapis.com/v1beta}") String url,
                           @Value("${embedding.api-key}") String apiKey) {
        this.restClient = builder.baseUrl(url).defaultHeader("x-goog-api-key", apiKey).build();
    }

    // One call can carry many texts. kind is RETRIEVAL_DOCUMENT for a product and RETRIEVAL_QUERY for a search.
    public List<float[]> embed(List<String> texts, String kind) {
        List<Map<String, Object>> requests = texts.stream().map(text -> Map.<String, Object>of(
                "model", "models/" + MODEL,
                "content", Map.of("parts", List.of(Map.of("text", text))),
                "taskType", kind,
                "outputDimensionality", 768)).toList();
        GeminiEmbeddingResponse response = restClient.post()
                .uri("/models/{model}:batchEmbedContents", MODEL)
                .body(Map.of("requests", requests))
                .retrieve()
                .body(GeminiEmbeddingResponse.class);
        return response.embeddings().stream().map(e -> toNumbers(e.values())).toList();
    }

    // The numbers are saved as one text, like 0.12,-0.03,0.51
    public static String toText(float[] numbers) {
        StringBuilder text = new StringBuilder();
        for (float number : numbers) {
            text.append(text.isEmpty() ? "" : ",").append(number);
        }
        return text.toString();
    }

    public static float[] toNumbers(String text) {
        String[] parts = text.split(",");
        float[] numbers = new float[parts.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Float.parseFloat(parts[i]);
        }
        return numbers;
    }

    private static float[] toNumbers(List<Float> values) {
        float[] numbers = new float[values.size()];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = values.get(i);
        }
        return numbers;
    }
}
