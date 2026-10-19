package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Double getAvgPriceForCatgeory(String categoryName) {
        return categoryRepository.findAvgPriceForCategory(categoryName);
    }
}
