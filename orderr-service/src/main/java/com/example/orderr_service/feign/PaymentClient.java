package com.example.orderr_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.orderr_service.dto.PaymentRequest;
import com.example.orderr_service.dto.PaymentResponse;



@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/payments")
    PaymentResponse processPayment(@RequestBody PaymentRequest paymentRequest);
}