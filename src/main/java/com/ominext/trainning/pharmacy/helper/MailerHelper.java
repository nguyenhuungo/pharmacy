package com.ominext.trainning.pharmacy.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MailerHelper
 */
@Slf4j
@Component
public class MailerHelper {

    @Value("${mail.noreply.address}")
    private String fromEmailAddress;

    private final JavaMailSender mailSender;

    public MailerHelper(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(SendEmail item) {
        sendEmailViaJavaMail(item);
    }

    /**
     * Send email type html using JavaMail
     *
     * @param item item
     */
    private void sendEmailViaJavaMail(SendEmail item) {
        List<String> toEmail = item.getTo();
        List<String> ccEmail = item.getCc();
        List<String> bccEmail = item.getBcc();

        if (CollectionUtils.isEmpty(toEmail) && CollectionUtils.isEmpty(ccEmail) && CollectionUtils.isEmpty(bccEmail)) {
            return;
        }

        String subject = item.getSubject();
        String textBody = item.getBody();

        try {
            // Send the email.
            mailSender.send(mimeMessage -> {
                InternetAddress[] to = convertToArrayInternetAddress(toEmail);
                InternetAddress[] cc = convertToArrayInternetAddress(ccEmail);
                InternetAddress[] bcc = convertToArrayInternetAddress(bccEmail);

                // Assemble the email.
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setFrom(fromEmailAddress);
                if (to.length > 0) {
                    helper.setTo(to);
                }
                if (cc.length > 0) {
                    helper.setCc(cc);
                }
                if (bcc.length > 0) {
                    helper.setBcc(bcc);
                }
                helper.setSubject(subject);
                helper.setText(textBody, true);
            });
        } catch (Exception e) {
            log.error("sendEmailViaJavaMail {} error: {}", item.toString(), e.toString());
        }
    }

    private InternetAddress[] convertToArrayInternetAddress(List<String> emails) {
        Set<InternetAddress> addrs = emails.stream().map(email -> {
            InternetAddress addr = new InternetAddress();
            addr.setAddress(email);
            return addr;
        }).collect(Collectors.toSet());

        InternetAddress[] to = new InternetAddress[addrs.size()];
        return addrs.toArray(to);
    }
}
