package com.ominext.trainning.pharmacy.model;

import com.ominext.trainning.pharmacy.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    @JoinColumn(name = "phone_number")
    private String phoneNumber;
    private OrderStatus status;
    private String name;
    @Column(name = "total_price")
    private double totalPrice;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Account account;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
}
