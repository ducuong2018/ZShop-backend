package com.example.zshop.controllers;

import com.example.zshop.models.LoginDTO;
import com.example.zshop.models.RegisterDTO;
import com.example.zshop.repositories.UserRepository;
import com.example.zshop.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping("/v1/auth")
@CrossOrigin("http://localhost:3000")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO ) throws IOException { return userService.saveUser(registerDTO); }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
    @PostMapping("/sendOtp")
    public ResponseEntity<?> sendOtp(@RequestBody RegisterDTO registerDTO) throws  IOException {return  userService.addUserDatabase(registerDTO);}

}
