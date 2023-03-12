package com.ominext.trainning.pharmacy.service.payBillService;


import com.ominext.trainning.pharmacy.exception.BasicException;
import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.model.Order;
import com.ominext.trainning.pharmacy.model.OrderDetail;
import com.ominext.trainning.pharmacy.repository.OrderDetailRepository;
import com.ominext.trainning.pharmacy.repository.OrderRepository;
import com.ominext.trainning.pharmacy.repository.AccountRepository;
import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;
import com.ominext.trainning.pharmacy.security.user.UserLoginInfo;
import com.ominext.trainning.pharmacy.service.AccountService;
import com.ominext.trainning.pharmacy.service.OrderService;
import com.ominext.trainning.pharmacy.service.mailService.MailService;
import com.ominext.trainning.pharmacy.utils.constant.AutoPassGeneraror;
import com.ominext.trainning.pharmacy.utils.constant.BaseConst;
import com.ominext.trainning.pharmacy.utils.constant.StringConst;
import com.ominext.trainning.pharmacy.utils.constant.Validate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

import static com.ominext.trainning.pharmacy.enums.OrderStatus.NEW;
import static com.ominext.trainning.pharmacy.enums.OrderStatus.PROCESSING;

@Service
public class PayBillServiceIml implements PayBillService {
    private final AccountService accountService;
    private final OrderService orderService;
    private final HttpSession session;
    private final MailService mailService;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final OrderDetailRepository orderDetailRepository;

    public PayBillServiceIml(AccountService accountService, OrderService orderService, HttpSession session,
                             MailService mailService, OrderRepository orderRepository, AccountRepository accountRepository,
                             OrderDetailRepository orderDetailRepository) {
        this.accountService = accountService;
        this.orderService = orderService;
        this.session = session;
        this.mailService = mailService;
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public void payTheBill(AccountRegistrationDto registrationDto) {
        UserLoginInfo userLoginInfo = (UserLoginInfo) session.getAttribute(BaseConst.USER_SESSION);

        Account account;
        if (userLoginInfo != null) {
            account = this.accountService.findAccountByEmail(userLoginInfo.getEmail());
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(!Validate.isValidEmailAddress(registrationDto.getEmail())){
                throw new BasicException("Email không hợp lệ");
            }
            boolean existedAccount = accountRepository.existsByEmailAndDeletedFalse(registrationDto.getEmail());
            if (existedAccount) {
                account = this.accountService.findAccountByEmail(registrationDto.getEmail());
            } else {
                String pass = AutoPassGeneraror.AutoPass(6);
                registrationDto.setRole(StringConst.ROLE_USER);
                registrationDto.setPassword(encoder.encode(pass));
                account = this.accountService.save(registrationDto);
            }
        }

        Order order = this.orderRepository.findFirstByAccountAndStatus(account, NEW);
        if (order != null) {
            order.setStatus(PROCESSING);
            this.orderRepository.save(order);
        } else {
            this.orderService.createOrder(account);
        }

        // TODO send mail
    }

    @Override
    public Map<String, Object> view() {
        Map<String, Object> map = new HashMap<>();
        UserLoginInfo userLoginInfo = (UserLoginInfo) session.getAttribute(BaseConst.USER_SESSION);
        if (userLoginInfo != null) {
            Account account = this.accountService.findAccountByEmail(userLoginInfo.getEmail());
            map.put("account", account);
            Order order = this.orderRepository.findFirstByAccountAndStatus(account, NEW);
            List<OrderDetail> orderDetails = this.orderDetailRepository.findAllByOrder(order);
            if (orderDetails == null) {
                return null;
            } else {
                map.put("order_detail", orderDetails);
            }

        } else {
            List<OrderDetail> orderDetails = new ArrayList<>();
            HashMap<Long, OrderDetail> productListSession = (HashMap<Long, OrderDetail>)
                    this.session.getAttribute("productListSession");
            if (productListSession == null) {
                return null;
            } else {
                productListSession.forEach((key, value) -> {
                    orderDetails.add(value);
                });
                Account account = new Account();
                map.put("order_detail", orderDetails);
                map.put("account", account);
            }
        }
        return map;
    }

}
