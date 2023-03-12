package com.ominext.trainning.pharmacy.service.payBillService;

import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;

import javax.mail.MessagingException;
import java.util.Map;

public interface PayBillService {
    void payTheBill(AccountRegistrationDto registrationDto) throws MessagingException;
    Map<String, Object> view();
}
