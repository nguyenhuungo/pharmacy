package com.ominext.trainning.pharmacy.repository;

import com.ominext.trainning.pharmacy.enums.OrderStatus;
import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT max(o.id) FROM Order o")
    Long findMaxId();
    Order findAllById(Long id);
    Order findFirstByAccountAndStatus(Account account, OrderStatus status);
}
