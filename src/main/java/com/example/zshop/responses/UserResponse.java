package com.example.zshop.responses;

import com.example.zshop.entities.User;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String phone_number;
}
