package com.example.zshop.services;

import com.example.zshop.data.TokenInfo;
import com.example.zshop.exceptions.BadRequestException;
import com.example.zshop.exceptions.Message;
import com.example.zshop.jwt.JwtTokenProvider;
import com.example.zshop.responses.DataResponse;
import com.example.zshop.entities.User;
import com.example.zshop.models.LoginDTO;
import com.example.zshop.models.RegisterDTO;
import com.example.zshop.repositories.UserRepository;
import com.example.zshop.responses.LoginResponse;
import com.example.zshop.security.CustomUserDetails;
import com.example.zshop.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Autowired protected AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Override
    public UserDetails loadUserByUsername(String email){
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            throw new BadRequestException(Message.USER_NOT_EXITED);
        }

        return new CustomUserDetails(user);
    }
    public UserDetails loadUserById(Long id){
        User user = userRepository.findUserById(id);
        return new CustomUserDetails(user);
    }
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public ResponseEntity<?> saveUser(RegisterDTO registerDTO) {
        User user;
        user = checkEmail(registerDTO.getEmail());
        if(Objects.nonNull(user)){
            throw new BadRequestException(Message.USERNAME_EXITED);
        }
        user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassWord(bCryptPasswordEncoder.encode(registerDTO.getPassword()));
        userRepository.save(user);
        DataResponse response = new DataResponse();
        response.setMessage("Thêm tài khoản thành công!");
        return ResponseEntity.ok(response);
    }
    private User checkEmail(String email){
        User user;
        if(Helpers.regexEmail(email)){
            user =  userRepository.findUserByEmail(email);
        }
        else {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        return  user;
    }
    public ResponseEntity<?> login(LoginDTO loginDTO){
        LoginResponse loginResponse;
        User user = checkEmail(loginDTO.getEmail());
        if(Objects.isNull(user)){
            throw new BadRequestException(Message.USERNAME_NOT_EXITED);
        }
        try {
            loginResponse = getLoginResponse(loginDTO,user);

        }
        catch (Exception e) {
            throw new BadRequestException(Message.PASSWORD_INVALID);
        }
        return ResponseEntity.ok(loginResponse);
    }

    protected LoginResponse getLoginResponse(LoginDTO loginDTO, User user){
        LoginResponse loginResponse = new LoginResponse();
        TokenInfo tokenInfo = new TokenInfo();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        tokenInfo.assignForm(user.getId());
        String token = jwtTokenProvider.generateToken(tokenInfo.getUserId());
        String refreshToken = "";
        loginResponse.assignForm(token, refreshToken);
        return loginResponse;
    }

}