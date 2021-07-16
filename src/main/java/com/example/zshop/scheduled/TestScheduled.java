package com.example.zshop.scheduled;

import com.example.zshop.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class TestScheduled {
    @Autowired
    UserService userService;
    @Scheduled(fixedRate = 2000, initialDelay = 2000)
    public void sendEmail(){
        log.info("Chúc bạn ngủ ngon nhà cường!");
    }
}
