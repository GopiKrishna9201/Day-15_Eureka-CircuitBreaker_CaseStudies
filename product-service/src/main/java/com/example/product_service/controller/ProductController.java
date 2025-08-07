package com.example.product_service.controller;

import com.example.product_service.entity.Product;
import com.example.product_service.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @PostMapping
    public Product addProduct(@RequestBody Product p) {
        return repo.save(p);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return repo.findById(id).orElse(null);
    }

    @GetMapping
    public List<Product> getAll() {
        return repo.findAll();
    }

    @PutMapping("/{id}/reduceStock")
    public ResponseEntity<String> reduceStock(@PathVariable String id, @RequestParam int qty) {
        Product product = repo.findById(id).orElse(null);
        if (product != null && product.getStock() >= qty) {
            product.setStock(product.getStock() - qty);
            repo.save(product);
            return ResponseEntity.ok("Stock reduced.");
        }
        return ResponseEntity.badRequest().body("Insufficient stock.");
    }
}
