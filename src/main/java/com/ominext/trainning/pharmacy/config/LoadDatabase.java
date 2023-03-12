package com.ominext.trainning.pharmacy.config;

import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * LoadDatabase
 */
@Configuration
public class LoadDatabase {

    private static final String USER_NAME = "admin";

    private static final String PASSWORD = "abc@1234";

    private static final String MAIL_ADDRESS = "haitv@ominext.com";

    @Bean
    public CommandLineRunner initDatabase(AccountRepository accountRepository) {
        return (args) -> {
            // Add Account
            createAccount(accountRepository);
        };
    }

    private void createAccount(AccountRepository accountRepository) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<Account> accounts = accountRepository.findAll();
        Account account = null;
        if (!CollectionUtils.isEmpty(accounts)) {
            account = accounts.get(0);
        }
        if (account == null) {
            account = new Account();
            account.setUsername(USER_NAME);
            account.setAuthorities("ROLE_ADMIN");
            account.setDeleted(false);
            String password = passwordEncoder.encode(PASSWORD);
            account.setPassword(password);
            account.setEmail(MAIL_ADDRESS);
            accountRepository.save(account);
        }
    }
}
