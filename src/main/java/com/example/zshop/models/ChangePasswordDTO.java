package com.example.zshop.models;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String email;
    private int otp;
    private String password;
    private String rePassword;
}
