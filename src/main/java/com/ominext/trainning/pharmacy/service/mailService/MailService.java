package com.ominext.trainning.pharmacy.service.mailService;

import com.ominext.trainning.pharmacy.enums.MailType;

import javax.mail.MessagingException;
import java.util.List;

public interface MailService {
    <T> void push(List<String> mailAddresses, List<String> ccMailAddresses, T param, MailType mailType) throws MessagingException;

}
