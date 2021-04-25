package com.example.zshop.services;

import com.example.zshop.data.TokenInfo;
import com.example.zshop.entities.User;
import com.example.zshop.exceptions.BadRequestException;
import com.example.zshop.exceptions.Message;
import com.example.zshop.jwt.JwtTokenProvider;
import com.example.zshop.models.*;
import com.example.zshop.responses.DataResponse;

import com.example.zshop.repositories.UserRepository;
import com.example.zshop.responses.LoginResponse;
import com.example.zshop.security.CustomUserDetails;
import com.example.zshop.utils.Helpers;
import com.example.zshop.utils.JsonParser;
import com.example.zshop.utils.Redis;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import static javafx.scene.input.KeyCode.T;

@Log4j2
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    Redis redis;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
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

//register
    public ResponseEntity<?> register(RegisterDTO registerDTO) throws IOException {
        if(Objects.nonNull(checkEmail(registerDTO.getEmail()))){
            throw new BadRequestException(Message.USERNAME_EXITED);
        }
        SendOtpDTO sendOtpDTO = new SendOtpDTO();
        int otp = randomOtp();
        sendEmailOtp(registerDTO.getEmail(),otp);
        sendOtpDTO.setOtp(otp);
        sendOtpDTO.setEmail(registerDTO.getEmail());
        sendOtpDTO.setPassword(registerDTO.getPassword());
        redis.setRedis(registerDTO.getEmail(),sendOtpDTO);
        DataResponse response = new DataResponse();
        response.setMessage("Gửi mã xác nhận thành công!");
        return ResponseEntity.ok(response);
    }
    public ResponseEntity<?> sendOtpRegister(SendOtpDTO sendOtpDTO) throws IOException{
        SendOtpDTO data = redis.getRedis(sendOtpDTO.getEmail(), SendOtpDTO.class);
        if(data.getOtp() != sendOtpDTO.getOtp()){
            throw  new BadRequestException(Message.OTP_NOT_VALID);
        }
        if(data.getEmail() !=  sendOtpDTO.getEmail()){
            throw new BadRequestException(Message.NOT_FOUND);
        }
        if(data.getPassword() !=  sendOtpDTO.getPassword()){
            throw new BadRequestException(Message.NOT_FOUND);
        }
            User user = new User();
            user.setEmail(data.getEmail());
            user.setPassWord(bCryptPasswordEncoder.encode(data.getPassword()));
            userRepository.save(user);
            DataResponse response = new DataResponse();
            response.setMessage("Thêm tài khoản thành công!");
            return ResponseEntity.ok(response);
    }
    private void sendEmailOtp(String email,int otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Mã xác nhận");
        msg.setText("Mã xác nhận của bạn"+otp);
        javaMailSender.send(msg);

    }
    private Integer randomOtp(){
        Random random = new Random();
        return random.nextInt(1000000);
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

    //login
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
        String token = jwtTokenProvider.generateToken(tokenInfo);
        String refreshToken = jwtTokenProvider.generateRefreshToken(tokenInfo);
        loginResponse.assignForm(token, refreshToken);
        return loginResponse;
    }

//    forgot
    public ResponseEntity<?> forgotPassword(ForgotDTO forgotDTO) throws IOException {
        if(Objects.isNull(checkEmail(forgotDTO.getEmail()))){
            throw new BadRequestException(Message.USER_NOT_EXITED);
        }
        int otp = randomOtp();
        sendEmailOtp(forgotDTO.getEmail(),otp);
        forgotDTO.setOtp(otp);
        redis.setRedis(forgotDTO.getEmail(), forgotDTO);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setMessage("Gửi mã xác nhận thành công!");
        return ResponseEntity.ok(dataResponse);
    }
    public ResponseEntity<?> changePassword(ChangePasswordDTO changePasswordDTO) throws IOException{
        ForgotDTO forgotDTO = redis.getRedis(changePasswordDTO.getEmail(), ForgotDTO.class);
        User user = userRepository.findUserByEmail(changePasswordDTO.getEmail());
        if(Objects.isNull(user)){
            throw new BadRequestException(Message.USER_NOT_EXITED);
        }
        if(forgotDTO.getOtp() != changePasswordDTO.getOtp()){
            throw new BadRequestException(Message.OTP_NOT_VALID);
        }
        if(!changePasswordDTO.getPassword().equals(changePasswordDTO.getRePassword())){
            throw new BadRequestException(Message.RE_PASSWORD);
        }
        user.setPassWord(bCryptPasswordEncoder.encode(changePasswordDTO.getPassword()));
        userRepository.save(user);
        DataResponse dataResponse = new DataResponse();
        dataResponse.setMessage("Thay đổi mật khẩu thành công");
        redis.del(forgotDTO.getEmail());
        return ResponseEntity.ok(dataResponse);
    }

}