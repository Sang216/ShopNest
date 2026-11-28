package com.shopnest.productcatalog.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SearchRequestDto {
    private String query;
    private BigDecimal maxPrice;
    private Integer page;
    private Integer size;
    private String sortBy;
}
