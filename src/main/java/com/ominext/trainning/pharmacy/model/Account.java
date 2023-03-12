package com.ominext.trainning.pharmacy.model;

import com.ominext.trainning.pharmacy.request.AccountRegistrationDto;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
public class Account extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String email;
  private String password;
  private boolean deleted;
  private String authorities;
  private String address;
  private String phone;

  public Account(AccountRegistrationDto registrationDto) {
    this.username = registrationDto.getUsername();
    this.password = registrationDto.getPassword();
    this.email = registrationDto.getEmail();
    this.deleted = false;
    this.authorities = registrationDto.getRole();
  }

  public Account() {
  }
}
