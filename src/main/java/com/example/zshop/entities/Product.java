package com.example.zshop.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "products",schema = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    private String name;
    private String image;
    private Long price;
    @Column(name = "price_sale")
    private Long priceSale;
    private String description;
    private int quantity;
}
