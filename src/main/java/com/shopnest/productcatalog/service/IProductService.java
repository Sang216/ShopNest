package com.shopnest.productcatalog.service;

import com.shopnest.productcatalog.model.Product;

public interface IProductService {

    Product getProductById(Long id);
}
