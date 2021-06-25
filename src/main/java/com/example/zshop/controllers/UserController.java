package com.example.zshop.controllers;

import com.example.zshop.entities.User;
import com.example.zshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("users")
    public ResponseEntity<?> getAllUser(){

        return userService.getAllUser();
    }

}
