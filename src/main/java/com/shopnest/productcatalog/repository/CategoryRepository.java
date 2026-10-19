package com.shopnest.productcatalog.repository;

import com.shopnest.productcatalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query(value = "SELECT AVG(p.price) from Product p where p.category.name=:categoryName")
    Double findAvgPriceForCategory(String categoryName);

    //    @Query(value = "SELECT avg(p.price) from product p JOIN category c " +
//            "ON p.category_id = c.id " +
//            "where c.name=:categoryName",nativeQuery = true)
//    Double findAvgPriceForCategory(String categoryName);
}
