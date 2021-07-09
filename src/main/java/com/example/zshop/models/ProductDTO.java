package com.example.zshop.models;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class ProductDTO {
    @NotNull
    private String name;
    @NotNull
    private String image;
    @NotNull
    private Long price;
    @NotNull
    private Long priceSale;
    @NotNull
    private String description;
    @NotNull
    private int quantity;
}
