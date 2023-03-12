package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.repository.ProductRepository;
import com.ominext.trainning.pharmacy.repository.impl.CustomProductRepositoryImpl;
import com.ominext.trainning.pharmacy.request.ProductRegistrationDto;
import com.ominext.trainning.pharmacy.request.SearchProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceIml implements ProductService {
    private final ProductRepository productRepository;

    private final CustomProductRepositoryImpl customProductRepository;

    public ProductServiceIml(ProductRepository productRepository, CustomProductRepositoryImpl customProductRepository) {
        this.productRepository = productRepository;
        this.customProductRepository = customProductRepository;
    }

    @Override
    public Product save(ProductRegistrationDto request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setImg(request.getImg());
        product.setContent(request.getContent());
        product.setDiscount(request.getDiscount());
        product.setPrice(request.getPrice());
        return this.productRepository.save(product);
    }

    @Override
    public Product findProductById(String id) {
        return this.productRepository.findAllById(Long.parseLong(id));
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAllByDeletedIsFalse();
    }

    @Override
    public void delete(Long id) {
        Product product = this.productRepository.findAllById(id);
        if (product != null) {
            product.setDeleted(true);
            this.productRepository.save(product);
        }
    }

    @Override
    public Page<Product> findByCondition(SearchProductRequest searchProductRequest, Pageable pageable) {
        return this.customProductRepository.findProductByCondition(searchProductRequest, pageable);
    }

    @Override
    public List<Product> findPopularProducts(int i) {
        return this.customProductRepository.findPopularProducts(i);
    }
}
