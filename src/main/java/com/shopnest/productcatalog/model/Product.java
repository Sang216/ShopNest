package com.shopnest.productcatalog.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product extends BaseEntity {

    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;

    private Category category;
}
