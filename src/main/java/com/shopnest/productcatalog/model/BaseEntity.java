package com.shopnest.productcatalog.model;

import com.shopnest.productcatalog.model.enums.State;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class BaseEntity {

    private Long id;
    private Date createdAt;
    private Date lastUpdatedAt;
    private State state;
}
