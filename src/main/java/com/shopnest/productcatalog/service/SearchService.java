package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.dto.SearchRequestDto;
import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.model.enums.State;
import com.shopnest.productcatalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;

    private static final Set<String> SORTABLE = Set.of("name", "price");
    private static final int MAX_SIZE = 50;

    private Pageable pageable(SearchRequestDto request) {
        String sortBy = request.getSortBy() == null ? "name" : request.getSortBy();
        if (!SORTABLE.contains(sortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can sort by name or price, not by '" + sortBy + "'.");
        }
        int page = request.getPage() == null ? 0 : Math.max(request.getPage(), 0);
        int size = request.getSize() == null ? 20 : Math.min(Math.max(request.getSize(), 1), MAX_SIZE);
        return PageRequest.of(page, size, Sort.by(sortBy).and(Sort.by("id")));
    }

    public Page<Product> search(SearchRequestDto searchRequestDto) {
        String word = StringUtils.hasText(searchRequestDto.getQuery()) ? '%' + searchRequestDto.getQuery().trim() +'%' : null;

        return productRepository.search(State.ACTIVE,
                word,
                searchRequestDto.getMaxPrice(),
                pageable(searchRequestDto));
    }
}
