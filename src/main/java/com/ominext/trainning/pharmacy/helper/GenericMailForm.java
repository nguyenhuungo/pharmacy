package com.ominext.trainning.pharmacy.helper;


import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * MailConnectFail
 */
@Data
@Builder
public class GenericMailForm {

    private String username;
    private String content;
    private String url;
}
