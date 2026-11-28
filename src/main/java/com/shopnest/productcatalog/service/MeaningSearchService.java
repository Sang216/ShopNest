package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.clients.EmbeddingClient;
import com.shopnest.productcatalog.dto.MeaningHitDto;
import com.shopnest.productcatalog.dto.SearchRequestDto;
import com.shopnest.productcatalog.model.enums.State;
import com.shopnest.productcatalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeaningSearchService {

    private static final Logger log = LoggerFactory.getLogger(MeaningSearchService.class);

    private final ProductRepository productRepo;
    private final EmbeddingClient embeddingClient;
    private final SearchService searchService;

    // The numbers of the sentence against the saved numbers of every product. The closest K come back.
    public List<MeaningHitDto> search(String text, int k) {
        try {
            float[] query = embeddingClient.embed(List.of(text), "RETRIEVAL_QUERY").get(0);
            return productRepo.findAllByState(State.ACTIVE).stream()
                    .filter(product -> product.getEmbedding() != null)
                    .map(product -> new MeaningHitDto(product.getName(),
                            cosine(query, EmbeddingClient.toNumbers(product.getEmbedding()))))
                    .sorted(Comparator.comparingDouble(MeaningHitDto::score).reversed())
                    .limit(k)
                    .toList();
        } catch (RestClientException e) {
            // The numbers service is down or slow. The customer still gets an answer, from the plain search.
            log.warn("Meaning search failed ({}). Using the plain search.", e.getMessage());
            SearchRequestDto request = new SearchRequestDto();
            request.setQuery(text);
            request.setSize(k);
            return searchService.search(request).map(product -> new MeaningHitDto(product.getName(), null)).getContent();
        }
    }

    static double cosine(float[] a, float[] b) {
        double dot = 0, lengthA = 0, lengthB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            lengthA += a[i] * a[i];
            lengthB += b[i] * b[i];
        }
        return dot / (Math.sqrt(lengthA) * Math.sqrt(lengthB));
    }
}
