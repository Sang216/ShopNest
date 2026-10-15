package com.shopnest.productcatalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseEntity {

    @Column(unique = true,nullable = false)
    private String name;
    
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products;  // frequent query of getting products based on category
}
