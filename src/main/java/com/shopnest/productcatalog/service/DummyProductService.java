package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.DummyJsonProductDto;
import com.shopnest.productcatalog.model.Category;
import com.shopnest.productcatalog.model.Product;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class DummyProductService implements IProductService {

    private RestTemplate restTemplate;

    public DummyProductService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public Product getProductById(Long id) {
        try {
            ResponseEntity<DummyJsonProductDto> response =
                    restTemplate.getForEntity("https://dummyjson.com/products/{id}", DummyJsonProductDto.class, id);

            if(response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return from(response.getBody());
            }
            return null;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    private Product from(DummyJsonProductDto dto) {

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getThumbnail());

        Category category = new Category();
        category.setName(dto.getCategory());
        product.setCategory(category);
        return product;
    }
}
