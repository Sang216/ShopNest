package com.shopnest.productcatalog.model;

import com.shopnest.productcatalog.model.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;
    private Date lastUpdatedAt;

    @Enumerated(value = EnumType.STRING)
    private State state; // ACTIVE, INACTIVE
}
