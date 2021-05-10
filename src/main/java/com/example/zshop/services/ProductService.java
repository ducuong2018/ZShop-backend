package com.example.zshop.services;

import com.example.zshop.entities.Product;
import com.example.zshop.exceptions.BadRequestException;
import com.example.zshop.exceptions.Message;
import com.example.zshop.models.ProductDTO;
import com.example.zshop.repositories.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@Service
@Log4j2
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public ResponseEntity<?> createProduct(ProductDTO productDTO) throws  Exception{
        if(Objects.isNull(productDTO.getName())) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        if(Objects.isNull(productDTO.getPrice())) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        if(Objects.isNull(productDTO.getDescription())) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        if(Objects.isNull(productDTO.getImage())) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        if(Objects.isNull(productDTO.getPrice_sale())) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        if(Objects.isNull(productDTO.getQuantity())) {
            throw new BadRequestException(Message.NOT_FOUND);
        }
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImage(productDTO.getImage());
        product.setPriceSale(productDTO.getPrice_sale());
        product.setQuantity(productDTO.getQuantity());
        return ResponseEntity.ok(productRepository.save(product));
    }
    public ResponseEntity<?> getProduct(){
        return  ResponseEntity.ok(productRepository.findAll());
    }
}
