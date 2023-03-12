package com.ominext.trainning.pharmacy.repository;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.request.SearchProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomProductRepository{
    Page<Product> findProductByCondition(SearchProductRequest searchProductRequest, Pageable pageable);

    List<Product> findPopularProducts(int i);
}
