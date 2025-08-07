package com.example.orderr_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orderr_service.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String>{

}
