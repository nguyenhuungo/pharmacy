package com.ominext.trainning.pharmacy.security.user;

import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.repository.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * UserDetailsServiceImpl
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check username
        if (StringUtils.isAllBlank(username)) {
            throw new BadCredentialsException("BaseConst.ERROR");
        }

        // Search the account by username
        Account account = accountRepository.findByEmailAndDeletedFalse(username);

        if (account == null) {
            throw new UsernameNotFoundException("BaseConst.NOT_FOUND");
        }

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(account.getAuthorities()));
        return new UserDetails(account.getId(), account.getEmail(), account.getPassword(), simpleGrantedAuthorities, account.getEmail());
    }
}
