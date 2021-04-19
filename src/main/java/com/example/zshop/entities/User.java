package com.example.zshop.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user",schema = "accounts")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password_crypt")
    private String passWord;
}
