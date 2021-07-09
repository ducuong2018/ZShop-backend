package com.example.zshop.responses;

import com.example.zshop.data.Metadata;
import lombok.Data;

import java.util.List;

@Data
public class ListUserResponse {
    List<UserResponse> users;
    Metadata metadata;
}
