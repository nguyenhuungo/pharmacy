package com.ominext.trainning.pharmacy.security.user;

import com.ominext.trainning.pharmacy.utils.constant.RequestPathConst;
import com.ominext.trainning.pharmacy.utils.constant.StringConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SecurityConfiguration
 */
@Configuration
@EnableWebSecurity
@Order(1)
public class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final UserSuccessHandler userSuccessHandler;
    private final UserFailureHandler userFailureHandler;
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;

    public UserSecurityConfiguration(UserDetailsServiceImpl userDetailsService, UserSuccessHandler userSuccessHandler,
                                     UserFailureHandler userFailureHandler, UserLogoutSuccessHandler userLogoutSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.userSuccessHandler = userSuccessHandler;
        this.userFailureHandler = userFailureHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().enableSessionUrlRewriting(true);

        http
                .requestMatchers().antMatchers("/SA/**").and().authorizeRequests()
            .antMatchers(RequestPathConst.SA001_LOGIN, RequestPathConst.SA001.concat("/**"), RequestPathConst.PAGE_ERROR, RequestPathConst.SA001_LOGOUT).permitAll()
            .antMatchers("/HO001/**", "/SA/**", "/SA002/**").hasRole(StringConst.USER).anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .accessDeniedHandler(userAccessDeniedHandler())
        ;

        http.formLogin()
            .loginPage(RequestPathConst.SA001)
            .loginProcessingUrl(RequestPathConst.SA001_LOGIN)
            .successHandler(userSuccessHandler)
            .failureHandler(userFailureHandler)
            .permitAll()
            .usernameParameter("username").passwordParameter("password")
            .and()
//            .rememberMe().key("uniqueAndSecret").tokenValiditySeconds(1296000) // 15 days expired
        ;

        http.logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID", "remember-me")
            .logoutRequestMatcher(new AntPathRequestMatcher(RequestPathConst.SA001_LOGOUT))
            .logoutSuccessHandler(userLogoutSuccessHandler)
            .permitAll()
            .and()
            .rememberMe()
            .key("uniqueAndSecret")
            .userDetailsService(userDetailsService)
        ;

        http.sessionManagement()
                .enableSessionUrlRewriting(true)
        ;

        http.csrf()
                .ignoringAntMatchers(RequestPathConst.SA, RequestPathConst.SA001, RequestPathConst.SA001_LOGIN, RequestPathConst.SA001_LOGOUT)
        ;

        http.headers()
                .contentTypeOptions()
        ;

        http.headers()
                .frameOptions()
                .sameOrigin()
        ;

        http.headers()
                .xssProtection()
                .xssProtectionEnabled(true)
        ;

        http.headers()
                .httpStrictTransportSecurity()
                .maxAgeInSeconds(31536000)
                .includeSubDomains(true)
        ;
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/images/**", "/js/**", "/css/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserAuthenticationConfigurer configurer = new UserAuthenticationConfigurer(userDetailsService).passwordEncoder(userPasswordEncoder());
        auth.apply(configurer);
    }
    
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder userPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler userAccessDeniedHandler() {
        return new UserAccessDeniedHandler();
    }
}
