package com.example.zshop.controllers;

import com.example.zshop.models.ProductDTO;
import com.example.zshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("product")
    public ResponseEntity<?> createProduct(@RequestHeader("x-token") String jwt,@RequestBody ProductDTO productDTO) throws Exception{
        return productService.createProduct(jwt,productDTO);
    }
    @GetMapping("product")
    public ResponseEntity<?> getProduct(@RequestHeader("x-token") String jwt, Pageable pageable){
        return  productService.getListProduct(jwt,pageable);
    }
}
