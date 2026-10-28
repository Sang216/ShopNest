package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.CategoryDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.mappers.ProductMapper;
import com.shopnest.productcatalog.model.Category;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.model.enums.State;
import com.shopnest.productcatalog.repository.CategoryRepository;
import com.shopnest.productcatalog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private StorageProductService storageProductService;

    // ---------- Positive ----------

    @Test
    void createProduct_shouldSaveAndReturnActiveProductWithCategory() {
        // given
        ProductDto dto = new ProductDto();
        dto.setName("Wireless Mouse");
        dto.setDescription("Ergonomic wireless mouse");
        dto.setPrice(new BigDecimal("29.99"));
        dto.setImageUrl("http://example.com/mouse.png");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Electronics");
        categoryDto.setDescription("Electronic accessories");
        dto.setCategory(categoryDto);

        Category existingCategory = new Category();
        existingCategory.setId(5L);
        existingCategory.setName("Electronics");
        existingCategory.setDescription("Electronic accessories");

        Product product = new Product();
        product.setName("Wireless Mouse");
        product.setDescription("Ergonomic wireless mouse");
        product.setPrice(new BigDecimal("29.99"));
        product.setImageUrl("http://example.com/mouse.png");
        product.setCategory(existingCategory);

        when(productMapper.toEntity(dto)).thenReturn(product);
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(existingCategory));
        when(productRepository.save(product)).thenReturn(product);

        // when
        Product result = storageProductService.createProduct(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Wireless Mouse");
        assertThat(result.getDescription()).isEqualTo("Ergonomic wireless mouse");
        assertThat(result.getPrice()).isEqualByComparingTo("29.99");
        assertThat(result.getImageUrl()).isEqualTo("http://example.com/mouse.png");

        assertThat(result.getCategory()).isNotNull();
        assertThat(result.getCategory().getName()).isEqualTo("Electronics");

        assertThat(result.getState()).isEqualTo(State.ACTIVE);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getLastUpdatedAt()).isNotNull();

        verify(categoryRepository).findByName("Electronics");
        verify(productRepository).save(product);
    }

    // ---------- Negative ----------

    @Test
    void createProduct_shouldThrowWhenSavingProductFails() {
        // given
        ProductDto dto = new ProductDto();
        dto.setName("Wireless Mouse");
        dto.setDescription("Ergonomic wireless mouse");
        dto.setPrice(new BigDecimal("29.99"));

        Category category = new Category();
        category.setName("Electronics");

        Product product = new Product();
        product.setName("Wireless Mouse");
        product.setDescription("Ergonomic wireless mouse");
        product.setPrice(new BigDecimal("29.99"));
        product.setCategory(category);

        when(productMapper.toEntity(dto)).thenReturn(product);
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("DB save failed"));

        // when / then
        assertThatThrownBy(() -> storageProductService.createProduct(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB save failed");

        verify(productRepository).save(any(Product.class));
    }

    // ---------- Edge ----------

    @Test
    void createProduct_shouldSaveProductWithoutCategoryWhenDtoHasNoCategory() {
        // given
        ProductDto dto = new ProductDto();
        dto.setName("Product Without Category");
        dto.setDescription("Product that has no category");
        dto.setPrice(new BigDecimal("9.99"));
        // category intentionally left null

        Product product = new Product();
        product.setName("Product Without Category");
        product.setDescription("Product that has no category");
        product.setPrice(new BigDecimal("9.99"));
        // category intentionally left null

        when(productMapper.toEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        // when
        Product result = storageProductService.createProduct(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Product Without Category");
        assertThat(result.getPrice()).isEqualByComparingTo("9.99");
        assertThat(result.getCategory()).isNull();
        assertThat(result.getState()).isEqualTo(State.ACTIVE);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getLastUpdatedAt()).isNotNull();

        verify(categoryRepository, never()).findByName(anyString());
        verify(productRepository).save(product);
    }
}
