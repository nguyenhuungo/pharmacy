package com.ominext.trainning.pharmacy.helper;

import java.util.concurrent.Callable;

public class SendMailThread implements Callable<Boolean> {

    private final MailerHelper mailerHelper;

    private SendEmail sendEmail;

    public SendMailThread(MailerHelper mailerHelper,
                          SendEmail sendEmail) {
        this.mailerHelper = mailerHelper;
        this.sendEmail = sendEmail;
    }

    @Override
    public Boolean call() throws Exception {
        mailerHelper.sendEmail(sendEmail);
        return true;
    }
}
