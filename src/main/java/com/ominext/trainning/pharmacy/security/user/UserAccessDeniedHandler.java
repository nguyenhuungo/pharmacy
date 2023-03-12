package com.ominext.trainning.pharmacy.security.user;

import com.ominext.trainning.pharmacy.utils.constant.RequestPathConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UserAccessDeniedHandler
 */
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @Value("${pharmacy.base.url}")
    protected String baseUrl;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        if (response.isCommitted()) {
            return;
        }

        if (accessDeniedException instanceof MissingCsrfTokenException) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(RequestPathConst.PAGE_ERROR);
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect(baseUrl);
        }
    }
}
