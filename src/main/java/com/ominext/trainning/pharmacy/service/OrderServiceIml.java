package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.enums.OrderStatus;
import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.model.Order;
import com.ominext.trainning.pharmacy.model.OrderDetail;
import com.ominext.trainning.pharmacy.repository.OrderDetailRepository;
import com.ominext.trainning.pharmacy.repository.OrderRepository;
import com.ominext.trainning.pharmacy.security.user.UserLoginInfo;
import com.ominext.trainning.pharmacy.utils.constant.BaseConst;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceIml implements OrderService {

    private final OrderDetailService orderDetailService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final HttpSession session;

    public OrderServiceIml(OrderDetailService orderDetailService, OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, HttpSession session) {
        this.orderDetailService = orderDetailService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.session = session;
    }

    @Override
    public Order createOrder(Account user) {
        UserLoginInfo userLoginInfo = (UserLoginInfo) session.getAttribute(BaseConst.USER_SESSION);
        double price_total = 0;
        if (userLoginInfo == null) {
            Map<Long, OrderDetail> productListSession = (HashMap<Long, OrderDetail>) this.session.getAttribute("productListSession");
            if (productListSession.isEmpty()) {
                System.out.println("Chưa thấy sản phẩm nào trong giỏ hàng!");
                return null;
            }

            for (Map.Entry<Long, OrderDetail> orderDetailEntry : productListSession.entrySet()) {
                price_total += orderDetailEntry.getValue().getAmount();
            }

            Order order = new Order();
            order.setAddress(user.getAddress());
            order.setPhoneNumber(user.getPhone());
            order.setAccount(user);
            order.setStatus(OrderStatus.PROCESSING);
            order.setName(user.getUsername());
            order.setTotalPrice(price_total);
            List<OrderDetail> orderDetails = createOrderDetail(productListSession, order);
            order.setOrderDetails(orderDetails);
            this.orderRepository.save(order);
            this.orderDetailService.createOrderDetail(orderDetails);
            productListSession.clear();
            session.setAttribute("productListSession", productListSession);
            return order;
        } else {
            Order order = this.orderRepository.findFirstByAccountAndStatus(user, OrderStatus.NEW);
            List<OrderDetail> orderDetails = this.orderDetailRepository.findAllByOrder(order);
            if (orderDetails.isEmpty()) {
                System.out.println("Chưa thấy sản phẩm nào trong giỏ hàng!");
                return null;
            }
            for (OrderDetail orderDetail : orderDetails) {
                price_total += orderDetail.getAmount();
            }
            order.setStatus(OrderStatus.PROCESSING);
            order.setTotalPrice(price_total);
            this.orderRepository.save(order);
            this.orderDetailService.createOrderDetail(orderDetails);
            return order;
        }

    }

    private List<OrderDetail> createOrderDetail(Map<Long, OrderDetail> orderDetailList, Order order) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (HashMap.Entry<Long, OrderDetail> orderDetailEntry : orderDetailList.entrySet()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(orderDetailEntry.getValue().getProduct());
            orderDetail.setQuantity(orderDetailEntry.getValue().getQuantity());
            orderDetail.setAmount(orderDetailEntry.getValue().getAmount());
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }

    @Override
    public void deletedOrder(String ids) {
        Long id = Long.parseLong(ids);
        Order order = findOrderById(ids);
        if (order != null) {
            order.setId(id);
            order.setStatus(OrderStatus.FEEDBACK);
            updateOrder(order);
        }
    }

    @Override
    public List<Order> findAllOrder() {
        return this.orderRepository.findAll();
    }

    @Override
    public Order findOrderById(String ids) {
        Long id = Long.parseLong(ids);
        return this.orderRepository.findAllById(id);
    }

    @Override
    public void updateOrder(Order order) {
        this.orderRepository.save(order);
    }
}
