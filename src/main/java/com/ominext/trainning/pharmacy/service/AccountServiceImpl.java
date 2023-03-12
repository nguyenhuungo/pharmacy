package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.exception.BasicException;
import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.repository.AccountRepository;
import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account save(AccountRegistrationDto registrationDto) {
        boolean isEmailExisted = this.accountRepository.existsByEmailAndDeletedFalse(registrationDto.getEmail());
        if (isEmailExisted) {
            throw new BasicException("Email already existed!");
        }

        String prefix = "ROLE_";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account account = new Account();
        account.setAuthorities(prefix + registrationDto.getRole());
        account.setUsername(registrationDto.getUsername());
        account.setEmail(registrationDto.getEmail());
        account.setPassword(encoder.encode(registrationDto.getPassword()));
        account.setAddress(registrationDto.getAddress());
        account.setPhone(registrationDto.getPhone());
        return this.accountRepository.save(account);
    }

    @Override
    public List<Account> getAllUsers() {
        return accountRepository.findAllByDeletedFalse();
    }

    @Override
    public List<Account> getUsersFilter(String filter) {
        if (filter != null) {
            return accountRepository.findAllByDeletedFalseAndUsernameLikeIgnoreCase(filter);
        }
        return accountRepository.findAllByDeletedFalse();
    }

    @Override
    public Account findAccountById(Long id) {
        Account settingAccount = accountRepository.findByIdAndDeletedFalse(id);
        if (Objects.isNull(settingAccount)) {
            throw new BasicException("Account not existed!");
        }
        return settingAccount;
    }

    @Override
    public void updateUser(AccountRegistrationDto registrationDto) {
        Account account = this.accountRepository.findByIdAndDeletedFalse(registrationDto.getId());
        if (account == null) {
            throw new BasicException("Account does not exist!");
        }

        Account existedEmailAccount = this.accountRepository.findByEmailAndDeletedFalse(registrationDto.getEmail());
        if (Objects.nonNull(existedEmailAccount) && !account.getEmail().equals(registrationDto.getEmail())) {
            throw new BasicException("Email is registered!");
        }

        account.setEmail(registrationDto.getEmail());
        account.setUsername(registrationDto.getUsername());
        this.accountRepository.save(account);
    }

    @Override
    public void deleteUser(Long id) {
        this.accountRepository.findById(id).ifPresent(account -> {
            account.setDeleted(true);
        });
    }

    @Override
    public Account findAccountByEmail(String email) {
        return this.accountRepository.findByEmailAndDeletedFalse(email);
    }
}
