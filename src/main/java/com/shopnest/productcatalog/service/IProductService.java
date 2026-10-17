package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.model.Product;

import java.util.List;

public interface IProductService {

    Product getProductById(Long id);
    List<Product> getAllProducts();

    Product createProduct(ProductDto dto);

    boolean deleteProductById(Long id);

    Product update(Long id, ProductDto dto);

    Product patch(Long id, ProductDto dto);
}
