package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.CategoryDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.mappers.ProductMapper;
import com.shopnest.productcatalog.model.Category;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.model.enums.State;
import com.shopnest.productcatalog.repository.CategoryRepository;
import com.shopnest.productcatalog.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Primary
@Slf4j
public class StorageProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public StorageProductService(ProductRepository productRepository,
                                 CategoryRepository categoryRepository,
                                 ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product getProductById(Long id) {
        log.info("Reading from DB.. ");
        return productRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        product.setCategory(findOrCreateCategory(product.getCategory()));
        product.setCreatedAt(new Date());
        product.setLastUpdatedAt(new Date());
        product.setState(State.ACTIVE);

        return productRepository.save(product);
    }

    public Product update(Long id, ProductDto dto) {

        Product product = productRepository.findByIdAndState(id, State.ACTIVE).orElse(null);

        if (product != null) {
            product.setName(dto.getName());
            product.setDescription(dto.getDescription());
            product.setPrice(dto.getPrice());
            product.setImageUrl(dto.getImageUrl());
            product.setLastUpdatedAt(new Date());

            if (dto.getCategory() != null) {
                Category category = fromCategory(dto.getCategory());
                product.setCategory(findOrCreateCategory(category));
            }
            productRepository.save(product);
        }
        return product;
    }

    public Product patch(Long id, ProductDto dto) {

        Product product = productRepository.findByIdAndState(id, State.ACTIVE).orElse(null);
        if (product == null) {
            return null;
        }

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getImageUrl() != null) {
            product.setImageUrl(dto.getImageUrl());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getCategory() != null) {
            Category category = fromCategory(dto.getCategory());
            product.setCategory(findOrCreateCategory(category));
        }

        product.setLastUpdatedAt(new Date());

        return productRepository.save(product);
    }

    @Override
    public Page<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetweenAndState(minPrice,
                maxPrice,
                State.ACTIVE,
                pageable);
    }

    private Category fromCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return category;
    }

    @Override
    public boolean deleteProductById(Long id) {
        Product product = productRepository.findByIdAndState(id, State.ACTIVE).orElse(null);
        if(product==null) return false;
        product.setState(State.INACTIVE);
        product.setLastUpdatedAt(new Date());
        productRepository.save(product);
        return true;
    }

    private Category findOrCreateCategory(Category category) {

        if (category == null) return null;

        Optional<Category> existing = categoryRepository.findByName(category.getName());
        if (existing.isPresent()) {
            return existing.get();
        }

        category.setId(null);
        category.setCreatedAt(new Date());
        category.setLastUpdatedAt(new Date());
        category.setState(State.ACTIVE);
        return categoryRepository.save(category);
    }
}
