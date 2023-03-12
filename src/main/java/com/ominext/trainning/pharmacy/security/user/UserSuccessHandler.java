package com.ominext.trainning.pharmacy.security.user;

import com.ominext.trainning.pharmacy.utils.constant.BaseConst;
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
public class UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${pharmacy.base.url}")
    protected String baseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        UserDetails simpleUser = (UserDetails) authentication.getPrincipal();
        UserLoginInfo userLoginInfo = new UserLoginInfo(simpleUser);
        request.getSession().setAttribute(BaseConst.USER_SESSION, userLoginInfo);
        redirectResponse(response);
    }

    /**
     * Send redirect response with result checking login success or not
     */
    private void redirectResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(baseUrl + RequestPathConst.SA002_DASHBOARD);
    }
}
