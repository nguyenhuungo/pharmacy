package com.ominext.trainning.pharmacy.enums;

import com.ominext.trainning.pharmacy.utils.constant.StringConst;
import lombok.Getter;

/**
 * Authorities
 */
@Getter
public enum OrderStatus {
    NEW(StringConst.ZERO, StringConst.NEW),
    PROCESSING(StringConst.ONE, StringConst.PROCESSING),
    DONE(StringConst.TWO, StringConst.DONE),
    FEEDBACK(StringConst.THREE, StringConst.FEEDBACK);

    private final String type;
    private final String text;

    public static OrderStatus getOrderStatus(String type) {
        switch (type) {
            case StringConst.ZERO:
                return NEW;
            case StringConst.ONE:
                return PROCESSING;
            case StringConst.TWO:
                return DONE;
            default:
                return FEEDBACK;
        }
    }

    OrderStatus(String type, String text) {
        this.type = type;
        this.text = text;
    }
}
