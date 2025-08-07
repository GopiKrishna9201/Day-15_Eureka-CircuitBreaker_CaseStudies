package com.example.userprofileservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.userprofileservice.entity.User;
import com.example.userprofileservice.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }
}
