package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.CategoryDto;
import com.shopnest.productcatalog.dto.DummyJsonProductDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImportService {

    private final IProductService storageService;
    private final IProductService supplierService;

    public ProductImportService(IProductService storageService,
                                @Qualifier("dummyProductService") IProductService supplierService) {
        this.storageService = storageService;
        this.supplierService = supplierService;
    }

    public int importProducts() {
        int count = 0;
        for (Product supplierProduct : supplierService.getAllProducts()) {
            ProductDto dto = toImportDto(supplierProduct);
            storageService.createProduct(dto);
            count++;
        }

        return count;
    }

    private ProductDto toImportDto(Product product) {
        ProductDto dto = new ProductDto();
        //   dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            // categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            dto.setCategory(categoryDto);
        }
        return dto;
    }
}
