package com.shopnest.productcatalog.controller;

import com.shopnest.productcatalog.dto.CategoryDto;
import com.shopnest.productcatalog.dto.PitchResponseDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.service.IProductService;
import com.shopnest.productcatalog.service.PitchNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;
    private final PitchNotifyService pitchNotifyService;
//    private final IProductService supplierService;

    public ProductController(IProductService productService,
                             PitchNotifyService pitchNotifyService) {
//                             @Qualifier("dummyProductService") IProductService supplierService) {
        this.productService = productService;
        this.pitchNotifyService = pitchNotifyService;
//        this.supplierService = supplierService;
    }

    @GetMapping
    List<ProductDto> getProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> dtos = new ArrayList<>();
        for (Product product: products) {
            dtos.add(from(product));
        }
        return dtos;
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        if (id < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(from(product), HttpStatus.OK);
    }

    @GetMapping("{id}/pitch")
    private ResponseEntity<PitchResponseDto> getPitchMessage(@PathVariable Long id) {
        if(id<0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String message = pitchNotifyService.getPitchNotifyMessage(product);

        PitchResponseDto dto = new PitchResponseDto();
        dto.setId(product.getId());
        dto.setMessage(message);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) {
        Product product = productService.createProduct(dto);
        return new ResponseEntity<>(from(product),HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        Product product = productService.update(id, dto);
        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(from(product),HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProductDto> patchProduct(@PathVariable Long id,  @RequestBody ProductDto dto) {
        Product product = productService.patch(id, dto);
        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(from(product),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProductById(id);
        if (isDeleted) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ProductDto from(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            CategoryDto category = new CategoryDto();
            category.setId(product.getCategory().getId());
            category.setName(product.getCategory().getName());
            category.setDescription(product.getDescription());

            dto.setCategory(category);
        }
        return dto;
    }
}
