package com.ominext.trainning.pharmacy.request;

import lombok.Data;

@Data
public class AccountRegistrationDto {
  private Long id;
  private String username;
  private String email;
  private String address;
  private String role;
  private String password;
  private String phone;
}