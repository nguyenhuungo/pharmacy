package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.model.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Account user);
    void deletedOrder(String id);
    List<Order> findAllOrder();
    Order findOrderById(String id);
    void updateOrder(Order order);
}
