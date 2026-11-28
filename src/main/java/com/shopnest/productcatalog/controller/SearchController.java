package com.shopnest.productcatalog.controller;

import com.shopnest.productcatalog.dto.MeaningHitDto;
import com.shopnest.productcatalog.dto.ProductDto;
import com.shopnest.productcatalog.dto.SearchRequestDto;
import com.shopnest.productcatalog.mappers.ProductMapper;
import com.shopnest.productcatalog.service.MeaningSearchService;
import com.shopnest.productcatalog.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final ProductMapper productMapper;
    private final MeaningSearchService meaningSearchService;

    @PostMapping
    public PagedModel<ProductDto> postSearch(@RequestBody SearchRequestDto searchRequestDto) {
        return new PagedModel<>(searchService.search(searchRequestDto)
                .map(productMapper::toDto));
    }

    @GetMapping
    public PagedModel<ProductDto> getSearch(SearchRequestDto searchRequestDto) {
        return new PagedModel<>(searchService.search(searchRequestDto)
                .map(productMapper::toDto));
    }

    @GetMapping("/meaning")
    public List<MeaningHitDto> meaning(@RequestParam String query) {
        return meaningSearchService.search(query, 5);
    }
}
