package com.ominext.trainning.pharmacy.response;

import lombok.Data;

@Data
public class AccountResponse {
    private String username;
    private String role;
    private String email;
    private boolean status;
    private boolean deleted;

    public AccountResponse() {}
    public AccountResponse(String username, String password, String email, String role, boolean status, boolean deleted) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
        this.deleted = deleted;
    }
}
