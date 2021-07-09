package com.example.zshop.responses;

import com.example.zshop.data.Metadata;
import com.example.zshop.entities.Product;
import lombok.Data;

import java.util.List;

@Data
public class ListProductResponse {
    List<Product> products;
    Metadata metadata;
}
