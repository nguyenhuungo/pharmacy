package com.ominext.trainning.pharmacy.service;

import com.ominext.trainning.pharmacy.model.Account;
import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;

import java.util.List;

public interface AccountService {
  Account save(AccountRegistrationDto registrationDto);
  Account findAccountById(Long id);
  List<Account> getAllUsers();
  List<Account> getUsersFilter(String filter);
  void updateUser(AccountRegistrationDto registrationDto);
  void deleteUser(Long id);
  Account findAccountByEmail(String email);
}
