package com.example.zshop.services;

import com.example.zshop.data.Metadata;
import com.example.zshop.entities.Product;
import com.example.zshop.exceptions.BadRequestException;
import com.example.zshop.exceptions.Message;
import com.example.zshop.jwt.JwtTokenProvider;
import com.example.zshop.models.ProductDTO;
import com.example.zshop.repositories.ProductRepository;
import com.example.zshop.responses.ListProductResponse;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Log4j2
@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    public ResponseEntity<?> createProduct(String jwt,ProductDTO productDTO){
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setImage(productDTO.getImage());
        product.setPrice(productDTO.getPrice());
        product.setPriceSale(productDTO.getPriceSale());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setUserId(jwtTokenProvider.getUserIdFromJWT(jwt));
        productRepository.save(product);
        return ResponseEntity.ok(product);
    }
    public ResponseEntity<?> getListProduct(String jwt,Pageable pageable){
        Long id = jwtTokenProvider.getUserIdFromJWT(jwt);
        Page<Product> productResponse = productRepository.findProductByUserId(id,pageable);
        List<Product> listProduct = new ArrayList<>();
        productResponse.stream().forEach(value -> {
            Product product = new Product();
            product.setId(value.getId());
            product.setQuantity(value.getQuantity());
            product.setUserId(value.getUserId());
            product.setDescription(value.getDescription());
            product.setPrice(value.getPrice());
            product.setPriceSale(value.getPriceSale());
            product.setImage(value.getImage());
            listProduct.add(product);
        });
        ListProductResponse listProductResponse = new ListProductResponse();
        listProductResponse.setProducts(listProduct);
        listProductResponse.setMetadata(new Metadata(productResponse));
        return  ResponseEntity.ok(listProductResponse);
    }
}
