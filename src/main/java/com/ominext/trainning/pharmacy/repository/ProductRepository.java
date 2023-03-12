package com.ominext.trainning.pharmacy.repository;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.request.SearchProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, CustomProductRepository {

    Product findAllById(Long id);

    List<Product> findAllByDeletedIsFalse();

}

