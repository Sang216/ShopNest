package com.shopnest.productcatalog.clients;

import com.shopnest.productcatalog.dto.DummyJsonProductDto;
import com.shopnest.productcatalog.dto.DummyJsonProductListDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DummyJsonClient {

    private final RestClient restClient;

    public DummyJsonClient(RestClient.Builder builder,
                           @Value("${dummyjson.url:https://dummyjson.com}") String dummyJsonUrl) {
        this.restClient = builder.baseUrl(dummyJsonUrl)
                .build();
    }

    public DummyJsonProductDto getProductById(Long id) {
        return restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(DummyJsonProductDto.class);
    }

    public DummyJsonProductListDto getAllProducts() {
        return restClient.get()
                .uri("/products?limit=0")
                .retrieve()
                .body(DummyJsonProductListDto.class);
    }
}
