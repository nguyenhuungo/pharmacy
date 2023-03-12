package com.ominext.trainning.pharmacy.security.admin;

import com.ominext.trainning.pharmacy.utils.constant.RequestPathConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LogoutSuccessHandler
 */
@Component
public class AdminLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${pharmacy.base.url}")
    protected String baseUrl;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String url = String.format("%s%s?logout", baseUrl, RequestPathConst.AM001);

        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(url);
    }
}
