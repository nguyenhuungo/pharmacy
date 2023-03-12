package com.ominext.trainning.pharmacy.repository;

import com.ominext.trainning.pharmacy.model.Order;
import com.ominext.trainning.pharmacy.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findAllByOrder(Order order);
}
