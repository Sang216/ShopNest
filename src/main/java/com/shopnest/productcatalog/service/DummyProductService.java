package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.clients.DummyJsonClient;
import com.shopnest.productcatalog.dto.DummyJsonProductDto;
import com.shopnest.productcatalog.dto.DummyJsonProductListDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.model.Category;
import com.shopnest.productcatalog.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DummyProductService implements IProductService {

    private final DummyJsonClient dummyJsonClient;

    public DummyProductService(DummyJsonClient dummyJsonClient) {
        this.dummyJsonClient = dummyJsonClient;
    }

    @Override
    public Product getProductById(Long id) {
        try {
            DummyJsonProductDto product = dummyJsonClient.getProductById(id);

            if (product != null) {
                return from(product);
            }
            return null;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        DummyJsonProductListDto response = dummyJsonClient.getAllProducts();

        List<Product> products = new ArrayList<>();
        for (DummyJsonProductDto dto : response.getProducts()) {
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
