package com.shopnest.productcatalog.controller;

import com.shopnest.productcatalog.service.ProductImportService;
import com.shopnest.productcatalog.service.StorageProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/products")
public class InternalController {

    private final ProductImportService productImportService;

    public InternalController(ProductImportService productService) {
        this.productImportService = productService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts() {
        int count = productImportService.importProducts();

        return ResponseEntity.ok("Imported " + count + " products");
    }
}
