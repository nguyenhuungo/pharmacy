package com.ominext.trainning.pharmacy.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Objects;

/**
 * MailContentBuilder
 */
@Component
public class MailContentBuilder {

    private static final String BASE_URL = "baseUrl";
    private static final String PARAM = "param";
    private static final String MAIL_TEMPLATE_PATH = "template/mail/%s";

    @Value("${pharmacy.base.url}")
    private String baseUrl;

    private final TemplateEngine templateEngine;

    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public <T> String buildTemplate(T parameter, String template) {
        Context context = new Context();
        context.setVariable(BASE_URL, baseUrl);
        if (Objects.nonNull(parameter)) {
            context.setVariable(PARAM, parameter);
        }
        return templateEngine.process(String.format(MAIL_TEMPLATE_PATH, template), context);
    }
}
