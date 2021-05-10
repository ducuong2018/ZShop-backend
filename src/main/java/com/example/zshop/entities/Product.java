package com.example.zshop.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products",schema = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String image;
    private Double price;
    @Column(name = "price_sale")
    private Double priceSale;
    private String description;
    private int quantity;
}
