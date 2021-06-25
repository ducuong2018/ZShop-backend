package com.example.zshop.controllers;

import com.example.zshop.models.*;
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


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO ) throws IOException { return userService.register(registerDTO); }
    @PostMapping("/sendOtp")
    public ResponseEntity<?> sendOtp(@RequestBody SendOtpDTO sendOtpDTO) throws  IOException {return  userService.sendOtpRegister(sendOtpDTO);}


    @PostMapping("forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotDTO forgotDTO )throws  IOException{
        return userService.forgotPassword(forgotDTO);
    }
    @PostMapping("forgot_change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) throws  IOException {
        return userService.changePassword(changePasswordDTO);
    }

}
