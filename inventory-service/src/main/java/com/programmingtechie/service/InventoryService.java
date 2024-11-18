package com.programmingtechie.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.programmingtechie.dto.InventoryResponse;


@Service
public interface InventoryService {
	
	public List<InventoryResponse> isInStock(List<String> skuCode) throws InterruptedException;

	

}
