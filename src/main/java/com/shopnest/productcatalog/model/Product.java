package com.shopnest.productcatalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(indexes = @Index(name = "idx_product", columnList = "name"))
public class Product extends BaseEntity {

    private String name;
    private String description;
    
    private String imageUrl;

    @Column(precision = 19, scale = 3)
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
}
