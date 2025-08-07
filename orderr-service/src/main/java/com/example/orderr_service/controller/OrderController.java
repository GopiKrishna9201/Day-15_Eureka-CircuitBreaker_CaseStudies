package com.example.orderr_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.orderr_service.dto.OrderRequest;
import com.example.orderr_service.dto.PaymentRequest;
import com.example.orderr_service.dto.PaymentResponse;
import com.example.orderr_service.dto.ProductResponse;
import com.example.orderr_service.entity.Order;

import com.example.orderr_service.feign.PaymentClient;
import com.example.orderr_service.feign.ProductClient;
import com.example.orderr_service.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired 
    private OrderRepository orderRepo;
    
    @Autowired 
    private ProductClient productClient;
    
    @Autowired 
    private PaymentClient paymentClient;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        ProductResponse product = productClient.getProductById(request.getProductId());
        if (product == null || product.getStock() < request.getQuantity()) {
            return ResponseEntity.badRequest().body("Product unavailable or insufficient stock.");
        }

        double total = product.getPrice() * request.getQuantity();
        String orderId = UUID.randomUUID().toString();

        Order order = new Order();
        order.setId(orderId);
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(total);
        order.setStatus("PENDING_PAYMENT");
        orderRepo.save(order);

        PaymentRequest paymentReq = new PaymentRequest();
        paymentReq.setOrderId(orderId);
        paymentReq.setAmount(total);
        paymentReq.setPaymentMethod(request.getPaymentMethod());

        PaymentResponse paymentResp = paymentClient.processPayment(paymentReq);

        if ("SUCCESS".equals(paymentResp.getStatus())) {
            order.setStatus("CONFIRMED");
            productClient.reduceStock(product.getId(), request.getQuantity());
        } else {
            order.setStatus("FAILED");
        }
        orderRepo.save(order);

        return ResponseEntity.ok(order);
    }
}