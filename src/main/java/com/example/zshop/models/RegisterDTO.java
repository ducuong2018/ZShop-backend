package com.example.zshop.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterDTO {
    @NotNull
    private String email;

    @NotNull
    private String password;

    private int otp;
}
