package com.example.zshop.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    private Long userId;

    public void assignForm(Long userId){
        setUserId(userId);
    }
}