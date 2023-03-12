package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.request.ProductRegistrationDto;
import com.ominext.trainning.pharmacy.request.SearchProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product save(ProductRegistrationDto productRegistrationDto);
    Product findProductById(String id);
    List<Product> findAll();

    void delete(Long id);
    Page<Product> findByCondition(SearchProductRequest searchProductRequest, Pageable pageable);

    List<Product> findPopularProducts(int i);
}
