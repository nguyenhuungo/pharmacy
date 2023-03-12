package com.ominext.trainning.pharmacy.enums;

import com.ominext.trainning.pharmacy.utils.constant.StringConst;
import lombok.Getter;

/**
 * MailType
 */
@Getter
public enum MailType {

    //AM
    PAY_BILL(StringConst.SEND_MAIL_PAY_BILL, StringConst.SEND_MAIL_PAY_BILL_SUBJECT);

    private final String fileName;
    private final String subject;

    MailType(String fileName, String subject) {
        this.fileName = fileName;
        this.subject = subject;
    }
}
