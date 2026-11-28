package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.clients.EmbeddingClient;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.model.enums.State;
import com.shopnest.productcatalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

// Runs once when the app starts with search.embed-on-start=true. Makes the numbers for every product, ahead of time.
@Component
@ConditionalOnProperty(name = "search.embed-on-start", havingValue = "true")
@RequiredArgsConstructor
public class ProductEmbedder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductEmbedder.class);

    private final ProductRepository productRepo;
    private final EmbeddingClient embeddingClient;
    long waitSeconds = 62;

    @Override
    public void run(ApplicationArguments args) {
        List<Product> products = productRepo.findAllByState(State.ACTIVE);
        for (int from = 0; from < products.size(); from += 100) {
            List<Product> batch = products.subList(from, Math.min(from + 100, products.size()));
            List<float[]> numbers = embedWithRetry(batch.stream().map(this::textOf).toList());
            for (int i = 0; i < batch.size(); i++) {
                batch.get(i).setEmbedding(EmbeddingClient.toText(numbers.get(i)));
                productRepo.save(batch.get(i));
            }
        }
        log.info("Made the numbers for {} products", products.size());
    }

    // The free key allows 100 texts a minute. When the numbers service says "too many requests", we wait and try again.
    private List<float[]> embedWithRetry(List<String> texts) {
        for (int attempt = 1; ; attempt++) {
            try {
                return embeddingClient.embed(texts, "RETRIEVAL_DOCUMENT");
            } catch (HttpClientErrorException.TooManyRequests e) {
                if (attempt == 3) {
                    throw e;
                }
                log.info("Too many requests. Waiting {} seconds, then trying again.", waitSeconds);
                try {
                    Thread.sleep(waitSeconds * 1000);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
    }

    private String textOf(Product product) {
        String category = product.getCategory() == null ? "" : " Category: " + product.getCategory().getName();
        return product.getName() + ". " + product.getDescription() + category;
    }
}