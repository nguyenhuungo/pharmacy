package com.ominext.trainning.pharmacy.enums;

import lombok.Getter;
import com.ominext.trainning.pharmacy.utils.constant.StringConst;

/**
 * Authorities
 */
@Getter
public enum Authorities {
    ROLE_ADMIN(StringConst.ZERO, StringConst.ROLE_ADMIN),
    ROLE_SUB_ADMIN(StringConst.ONE, StringConst.ROLE_SUB_ADMIN),
    ROLE_USER(StringConst.TWO, StringConst.ROLE_USER);

    private final String type;
    private final String text;
    
    public static Authorities getRole(String type) {
        switch (type) {
            case StringConst.ZERO:
                return ROLE_ADMIN;
            case StringConst.ONE:
                return ROLE_SUB_ADMIN;
            default:
                return ROLE_USER;
        }
    }

    Authorities(String type, String text) {
        this.type = type;
        this.text = text;
    }
}
