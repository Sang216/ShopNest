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

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;
    private PitchNotifyService pitchNotifyService;

    public ProductController(IProductService productService,
                             PitchNotifyService pitchNotifyService) {
        this.productService = productService;
        this.pitchNotifyService = pitchNotifyService;
    }

    @GetMapping
    List<Product> getProducts() {
        return List.of();
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

    private ProductDto from(Product product) {
        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());

        CategoryDto category = new CategoryDto();
        category.setName(product.getCategory().getName());
        category.setDescription(product.getDescription());

        dto.setCategory(category);
        return dto;
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
}
