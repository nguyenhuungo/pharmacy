package com.ominext.trainning.pharmacy.security.admin;

import com.ominext.trainning.pharmacy.utils.constant.RequestPathConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SuccessHandler
 */
@Component
public class AdminSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${pharmacy.base.url}")
    protected String baseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        AdminDetails simpleUser = (AdminDetails) authentication.getPrincipal();
        AdminLoginInfo adminLoginInfo = new AdminLoginInfo(simpleUser);
        redirectResponse(response);
    }

    /**
     * Send redirect response with result checking login success or not
     */
    private void redirectResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(baseUrl + RequestPathConst.AM002_DASHBOARD);
    }
}
