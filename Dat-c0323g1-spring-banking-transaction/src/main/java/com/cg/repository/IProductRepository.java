package com.cg.repository;

import com.cg.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product AS pr SET pr.price = :price WHERE pr.id = :id")
    void updatePrice(@Param("price") BigDecimal price, @Param("id") Long id);
}
