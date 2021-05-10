package com.example.zshop.models;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String image;
    private Double price;
    private Double price_sale;
    private String description;
    private int quantity;


}
