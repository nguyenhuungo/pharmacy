package com.ominext.trainning.pharmacy.controller.SA001;

import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.repository.AccountRepository;
import com.ominext.trainning.pharmacy.utils.constant.RequestPathConst;
import com.ominext.trainning.pharmacy.utils.constant.ScreenPathConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.ominext.trainning.pharmacy.utils.constant.RequestPathConst.LOGOUT;

@Controller
public class SA001Controller {
    @Value("${pharmacy.base.url}")
    protected String baseUrl;

    private final AccountRepository accountRepository;

    public SA001Controller(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping(RequestPathConst.SA001)
    public String login(@RequestParam(value = LOGOUT, required = false) String logout, HttpServletResponse response, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtils.isNotEmpty(request.getQueryString()) && request.getQueryString().equals("error")) {
            return ScreenPathConst.SA001_SCREEN;
        }
        if (logout != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        } else {
            if (Objects.nonNull(auth) && !(auth instanceof AnonymousAuthenticationToken)) {
                Account loggedInUser = accountRepository.findByEmailAndDeletedFalse(auth.getName());
                if (Objects.nonNull(loggedInUser)) {
                    return ScreenPathConst.REDIRECT + RequestPathConst.SA002_DASHBOARD;
                }
            }
        }
        return ScreenPathConst.REDIRECT.concat(baseUrl).concat(RequestPathConst.SA001_LOGIN);
    }

    @GetMapping(RequestPathConst.SA001_LOGIN)
    public String userLoginView() {
        return ScreenPathConst.SA001_SCREEN;
    }
}
