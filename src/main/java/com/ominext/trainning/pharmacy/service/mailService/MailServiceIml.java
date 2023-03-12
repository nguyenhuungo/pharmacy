package com.ominext.trainning.pharmacy.service.mailService;

import com.ominext.trainning.pharmacy.config.ThreadPoolManager;
import com.ominext.trainning.pharmacy.enums.MailType;
import com.ominext.trainning.pharmacy.helper.MailContentBuilder;
import com.ominext.trainning.pharmacy.helper.MailerHelper;
import com.ominext.trainning.pharmacy.helper.SendEmail;
import com.ominext.trainning.pharmacy.helper.SendMailThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class MailServiceIml implements MailService {
    private final MailerHelper mailerHelper;
    private final MailContentBuilder mailContentBuilder;
    private final ThreadPoolManager threadPoolManager;

    public MailServiceIml(MailerHelper mailerHelper, MailContentBuilder mailContentBuilder, ThreadPoolManager threadPoolManager) {
        this.mailerHelper = mailerHelper;
        this.mailContentBuilder = mailContentBuilder;
        this.threadPoolManager = threadPoolManager;
    }

    @Override
    public <T> void push(List<String> mailAddresses, List<String> ccMailAddresses, T param, MailType mailType) {
        try {
            String body = mailContentBuilder.buildTemplate(param, mailType.getFileName());
            String subject = mailType.getSubject();
            subject = String.format("%s", subject);

            SendEmail sendEmail = new SendEmail(mailAddresses, ccMailAddresses, null, subject, body);

            // Call thread send email
            SendMailThread thread = new SendMailThread(mailerHelper, sendEmail);
            Objects.requireNonNull(threadPoolManager.threadPoolExecutor().getObject()).submit(thread);
        } catch (Exception e) {
            log.error("Push email {} type {} fail: {}", mailAddresses, mailType, e.toString());
        }
    }
}
