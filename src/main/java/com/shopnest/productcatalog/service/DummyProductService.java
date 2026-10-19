package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.DummyJsonProductDto;
import com.shopnest.productcatalog.dto.DummyJsonProductListDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.model.Category;
import com.shopnest.productcatalog.model.Product;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<DummyJsonProductListDto> response = restTemplate.getForEntity(
                "https://dummyjson.com/products?limit=0", DummyJsonProductListDto.class);

        List<Product> products = new ArrayList<>();
        for (DummyJsonProductDto dto : response.getBody().getProducts()) {
            products.add(from(dto));
        }
        return products;
    }

    @Override
    public Product createProduct(ProductDto dto) {
        return null;
    }

    @Override
    public boolean deleteProductById(Long id) {
        return false;
    }

    @Override
    public Product update(Long id, ProductDto dto) {
        return null;
    }

    @Override
    public Product patch(Long id, ProductDto dto) {
        return null;
    }

    @Override
    public Page<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return null;
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
