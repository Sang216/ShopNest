package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.dto.SearchRequestDto;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.model.enums.State;
import com.shopnest.productcatalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;

    public Page<Product> search(SearchRequestDto searchRequestDto) {
        String word = StringUtils.hasText(searchRequestDto.getQuery()) ? '%' + searchRequestDto.getQuery().trim() +'%' : null;

        return productRepository.search(State.ACTIVE,
                word,
                searchRequestDto.getMaxPrice());
    }
}
