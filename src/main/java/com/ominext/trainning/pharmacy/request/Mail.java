package com.ominext.trainning.pharmacy.request;

import lombok.Data;

import java.util.Map;

@Data
public class Mail {
    String to;
    String subject;
    String text;
    String template;
    Map<String, Object> properties;
}
