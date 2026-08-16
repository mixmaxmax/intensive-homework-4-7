package com.homework.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<String> fallbackGet() {
        return fallbackResponse();
    }

    @PostMapping("/users")
    public ResponseEntity<String> fallbackPost() {
        return fallbackResponse();
    }

    @PutMapping("/users")
    public ResponseEntity<String> fallbackPut() {
        return fallbackResponse();
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> fallbackDelete() {
        return fallbackResponse();
    }

    private ResponseEntity<String> fallbackResponse() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("user-service is unavailable now");
    }
}