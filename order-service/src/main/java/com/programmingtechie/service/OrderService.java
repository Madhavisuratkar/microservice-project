package com.programmingtechie.service;

import org.springframework.stereotype.Service;

import com.programmingtechie.dto.OrderRequestDto;



@Service
public interface OrderService {

	public void placeOrder(OrderRequestDto orderRequestDto);
}