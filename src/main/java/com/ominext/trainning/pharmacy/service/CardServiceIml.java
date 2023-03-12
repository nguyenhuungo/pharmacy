package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.exception.BasicException;
import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.model.Order;
import com.ominext.trainning.pharmacy.model.OrderDetail;
import com.ominext.trainning.pharmacy.model.Product;
import com.ominext.trainning.pharmacy.repository.OrderDetailRepository;
import com.ominext.trainning.pharmacy.repository.OrderRepository;
import com.ominext.trainning.pharmacy.repository.ProductRepository;
import com.ominext.trainning.pharmacy.security.user.UserLoginInfo;
import com.ominext.trainning.pharmacy.utils.constant.BaseConst;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ominext.trainning.pharmacy.enums.OrderStatus.NEW;

@Service
public class CardServiceIml implements CardService {
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final HttpSession session;
    private final AccountService accountService;

    public CardServiceIml(ProductRepository productRepository, OrderDetailRepository orderDetailRepository,
                          OrderRepository orderRepository, HttpSession session, AccountService accountService) {
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.session = session;
        this.accountService = accountService;
    }

    @Override
    public String addToCard(String id, int quantity) {
        if (quantity <= 0) {
            throw new BasicException("Vui lòng chọn số lượng sản phẩm bạn muốn mua");
        }

        Product product = this.productRepository.findAllById(Long.parseLong(id));
        if (product == null) {
            throw new BasicException("Sản phẩm không tồn tại");
        }

        if (product.getQuantity() < quantity) {
            throw new BasicException("Số lượng sản phẩm không đủ");
        }

        UserLoginInfo userLoginInfo = (UserLoginInfo) session.getAttribute(BaseConst.USER_SESSION);
        if (userLoginInfo != null) {
            Account account = this.accountService.findAccountByEmail(userLoginInfo.getEmail());
            Order order = orderRepository.findFirstByAccountAndStatus(account, NEW);
            if (order != null) {
                List<OrderDetail> orderDetails = order.getOrderDetails();
                int i = 0;
                for (OrderDetail orderDetail : orderDetails) {
                    if (product.getId().equals(orderDetail.getProduct().getId())) {
                        i += 1;
                        int updateQuantity = orderDetail.getQuantity() + quantity;
                        double amount = product.getPrice() * updateQuantity - (product.getDiscount() * product.getPrice() / 100);
                        orderDetail.setQuantity(updateQuantity);
                        orderDetail.setAmount(amount);
                    }
                }
                if (i == 0) {
                    saveOrderDetail(quantity, product, order);
                }
                this.orderDetailRepository.saveAll(orderDetails);
            } else {
                Long orderId = this.orderRepository.findMaxId() + 1;

                order = new Order();
                order.setId(userLoginInfo.getId());
                order.setAddress(account.getAddress());
                order.setPhoneNumber(account.getPhone());
                order.setStatus(NEW);
                order.setId(orderId);
                order.setAccount(account);
                this.orderRepository.save(order);
                saveOrderDetail(quantity, product, order);
            }
        } else {
            Map<Long, OrderDetail> productListSession = (HashMap<Long, OrderDetail>) this.session.getAttribute("productListSession");
            if (productListSession == null) {
                productListSession = new HashMap<>();
            }
            OrderDetail orderDetail = new OrderDetail();
            double amount = product.getPrice() * quantity - (product.getDiscount() * product.getPrice() / 100);
            orderDetail.setQuantity(quantity);
            orderDetail.setAmount(amount);
            orderDetail.setProduct(product);
            orderDetail.setId(product.getId());
            productListSession.put(product.getId(), orderDetail);

            this.session.setAttribute("productListSession", productListSession);
            return "Success";
        }
        return "error";
    }

    private void saveOrderDetail(int quantity, Product product, Order order) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setAmount(product.getPrice() * quantity - (product.getDiscount() * product.getPrice() / 100));
        orderDetail.setOrder(order);
        this.orderDetailRepository.save(orderDetail);
    }

    @Override
    public void removeItemCard(String id) {
        UserLoginInfo userLoginInfo = (UserLoginInfo) session.getAttribute(BaseConst.USER_SESSION);
        Long idl = Long.parseLong(id);
        if (userLoginInfo != null) {
            this.orderDetailRepository.deleteById(idl);
        }
        HashMap<Long, OrderDetail> productListSession = (HashMap<Long, OrderDetail>) this.session.getAttribute("productListSession");
        productListSession.remove(idl);
        this.session.setAttribute("productListSession", productListSession);
    }

    @Override
    public double updateQuantity(String ids, int quantity) {
        Long id = Long.parseLong(ids);
        double quantityNew;
        Optional<OrderDetail> optionalOrderDetail = this.orderDetailRepository.findById(id);
        if (optionalOrderDetail.isPresent()) {
            OrderDetail orderDetail = optionalOrderDetail.orElseGet(OrderDetail::new);
            orderDetail.setQuantity(quantity);
            quantityNew = orderDetail.getProduct().getPrice() * quantity;
            orderDetail.setAmount(quantityNew);
            this.orderDetailRepository.save(orderDetail);
        } else {
            HashMap<Long, OrderDetail> productListSession = (HashMap<Long, OrderDetail>) this.session.getAttribute("productListSession");
            OrderDetail orderDetail = productListSession.get(id);
            quantityNew = 0;
            if (orderDetail != null) {
                orderDetail.setQuantity(quantity);
                quantityNew = orderDetail.getProduct().getPrice() * quantity;
                orderDetail.setAmount(quantityNew);
                productListSession.put(id, orderDetail);
                this.session.setAttribute("productListSession", productListSession);
            }
        }
        return quantityNew;
    }
}
