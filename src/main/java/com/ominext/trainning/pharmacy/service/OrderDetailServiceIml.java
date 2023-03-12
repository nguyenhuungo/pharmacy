package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.model.OrderDetail;
import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.repository.OrderDetailRepository;
import com.ominext.trainning.pharmacy.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceIml implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    public OrderDetailServiceIml(OrderDetailRepository orderDetailRepository, ProductRepository productRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void createOrderDetail(List<OrderDetail> orderDetails) {
        List<OrderDetail> list = this.orderDetailRepository.saveAll(orderDetails);

        for (OrderDetail orderDetail: list) {
            Product product = this.productRepository.findAllById(orderDetail.getProduct().getId());
            product.setQuantity(orderDetail.getQuantity());
            this.productRepository.save(product);
        }
    }
}
