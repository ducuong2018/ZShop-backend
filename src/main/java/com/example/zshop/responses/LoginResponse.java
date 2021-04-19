package com.example.zshop.responses;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;

    public void assignForm(String token, String refreshToken) {
        setRefreshToken(refreshToken);
        setToken(token);
    }
}