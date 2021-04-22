package com.example.zshop.services;

import com.example.zshop.data.TokenInfo;
import com.example.zshop.entities.User;
import com.example.zshop.exceptions.BadRequestException;
import com.example.zshop.exceptions.Message;
import com.example.zshop.jwt.JwtTokenProvider;
import com.example.zshop.responses.DataResponse;

import com.example.zshop.models.LoginDTO;
import com.example.zshop.models.RegisterDTO;
import com.example.zshop.repositories.UserRepository;
import com.example.zshop.responses.LoginResponse;
import com.example.zshop.security.CustomUserDetails;
import com.example.zshop.utils.Helpers;
import com.example.zshop.utils.JsonParser;
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
    public ResponseEntity<?> saveUser(RegisterDTO registerDTO) throws IOException {
        User user= checkEmail(registerDTO.getEmail());
        if(Objects.nonNull(user)){
            throw new BadRequestException(Message.USERNAME_EXITED);
        }
        int otp = randomOtp();
        registerDTO.setOtp(otp);
        sendEmailOtp(registerDTO.getEmail(),otp);
        redisTemplate.opsForValue().set(registerDTO.getEmail(), JsonParser.toJson(registerDTO));
        DataResponse response = new DataResponse();
        response.setMessage("Send otp!");
        return ResponseEntity.ok(response);
    }
    public ResponseEntity<?> addUserDatabase(RegisterDTO registerDTO) throws IOException{
        RegisterDTO data = (RegisterDTO) redisTemplate.opsForValue().get(registerDTO.getEmail());
        if(data.getOtp() != registerDTO.getOtp()){
            throw  new BadRequestException(Message.OTP_NOT_VALID);
        }
            User user = new User();
            user.setEmail(data.getEmail());
            user.setPassWord(bCryptPasswordEncoder.encode(data.getPassword()));
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
        String token = jwtTokenProvider.generateToken(tokenInfo);
        String refreshToken = jwtTokenProvider.generateRefreshToken(tokenInfo);
        loginResponse.assignForm(token, refreshToken);
        return loginResponse;
    }
    @Autowired
    private JavaMailSender javaMailSender;

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
//    void sendEmailWithAttachment() throws MessagingException, IOException {
//
//        MimeMessage msg = javaMailSender.createMimeMessage();
//
//        // true = multipart message
//        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
//
//        helper.setTo("to_@email");
//
//        helper.setSubject("Testing from Spring Boot");
//
//        // default = text/plain
//        //helper.setText("Check attachment for image!");
//
//        // true = text/html
//        helper.setText("<h1>Check attachment for image!</h1>", true);
//
//        // hard coded a file path
//        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));
//
//        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
//
//        javaMailSender.send(msg);
//
//    }
}