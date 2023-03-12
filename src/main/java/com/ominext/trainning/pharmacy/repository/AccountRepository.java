package com.ominext.trainning.pharmacy.repository;

import com.ominext.trainning.pharmacy.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Account findByEmailAndDeletedFalse(String email);

  Account findByIdAndDeletedFalse(Long id);

//  boolean existsByEmail(String email);

  List<Account> findAllByDeletedFalse();

  List<Account> findAllByDeletedFalseAndUsernameLikeIgnoreCase(String username);

  boolean existsById(Long id);

  boolean existsByEmailAndDeletedFalse(String email);

  class CustomProductRepositoryImpl {
  }
}
