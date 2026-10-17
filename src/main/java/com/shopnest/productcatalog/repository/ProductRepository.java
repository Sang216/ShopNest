package com.shopnest.productcatalog.repository;

import com.shopnest.productcatalog.model.Product;
import com.shopnest.productcatalog.model.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndState(Long id, State state);

//    List<Product> findAllProducts();
}
