package com.example.zshop.controllers;

import com.example.zshop.models.ProductDTO;
import com.example.zshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) throws Exception {
        return productService.createProduct(productDTO);
    }
    @GetMapping("/product")
    public ResponseEntity<?> getProduct(){
        return productService.getProduct();
    }
}
