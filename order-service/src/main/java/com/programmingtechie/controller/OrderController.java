package com.programmingtechie.controller;


import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.dto.OrderRequestDto;
import com.programmingtechie.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "inventory")
    public CompletableFuture<ResponseEntity<?>> placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            orderService.placeOrder(orderRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body("Order Placed Successfully");
        });
    }
    
    public CompletableFuture<ResponseEntity<?>> fallBackMethod(OrderRequestDto orderRequestDto, RuntimeException runTimeException) {
        return CompletableFuture.supplyAsync(() -> 
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                          .body("Oops! Something went wrong, please try ordering again later!")
        );
    }

}
