package com.ominext.trainning.pharmacy.security.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AuthenticationProvider
 */
@Slf4j
public class AdminAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {

        // Check password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.error("Admin account {} password {} incorrect", userDetails.getUsername(), password);
            throw new BadCredentialsException("BaseConst.ERROR");
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
