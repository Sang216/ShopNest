package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.model.Product;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IProductService {

    Product getProductById(UUID id);
    List<Product> getAllProducts();

    Product createProduct(ProductDto dto);

    boolean deleteProductById(UUID id);

    Product update(UUID id, ProductDto dto);

    Product patch(UUID id, ProductDto dto);

    Page<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
