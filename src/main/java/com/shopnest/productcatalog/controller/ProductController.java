package com.shopnest.productcatalog.controller;

import com.shopnest.productcatalog.dto.PitchResponseDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.mappers.ProductMapper;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.service.IProductService;
import com.shopnest.productcatalog.service.PitchNotifyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;
    private final PitchNotifyService pitchNotifyService;
    private final ProductMapper productMapper;

    public ProductController(IProductService productService,
                             PitchNotifyService pitchNotifyService,
                             ProductMapper productMapper) {
        this.productService = productService;
        this.pitchNotifyService = pitchNotifyService;
        this.productMapper = productMapper;
    }

    @GetMapping
    List<ProductDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> dtos = new ArrayList<>();
        for (Product product: products) {
            dtos.add(productMapper.toDto(product));
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
        return new ResponseEntity<>(productMapper.toDto(product), HttpStatus.OK);
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
        return new ResponseEntity<>(productMapper.toDto(product),HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        Product product = productService.update(id, dto);
        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(productMapper.toDto(product),HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProductDto> patchProduct(@PathVariable Long id,  @RequestBody ProductDto dto) {
        Product product = productService.patch(id, dto);
        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(productMapper.toDto(product),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProductById(id);
        if (isDeleted) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search")
    public PagedModel<ProductDto> getProductsInPriceRange(@RequestParam(required = false) BigDecimal minPrice,
                                              @RequestParam(required = false) BigDecimal maxPrice,
                                              Pageable pageable) {
        Page<ProductDto> products = productService
                .getProductsInPriceRange(minPrice,maxPrice,pageable)
                .map(productMapper::toDto);

        return new PagedModel<>(products);
    }
}
