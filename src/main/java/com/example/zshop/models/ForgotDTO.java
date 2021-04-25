package com.example.zshop.models;

import lombok.Data;

@Data
public class ForgotDTO {
    private String email;
    private int otp;
}
