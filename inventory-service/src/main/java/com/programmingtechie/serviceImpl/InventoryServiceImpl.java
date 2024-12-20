package com.programmingtechie.serviceImpl;


import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.dto.InventoryResponse;
import com.programmingtechie.repository.InventoryRepository;
import com.programmingtechie.service.InventoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService{
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	
	@Transactional(readOnly = true)
	@Override
	public List<InventoryResponse> isInStock(List<String> skuCode) throws InterruptedException {
//		log.info("Wait Started");
//		Thread.sleep(10000);
//		log.info("Wait Ended"); 
	    return inventoryRepository.findBySkuCodeIn(skuCode).stream()
	        .map(inventory -> 
	            InventoryResponse.builder()
	                .skuCode(inventory.getSkuCode())           // Set SKU code
	                .isInStock(inventory.getQuantity() > 0)    // Check if quantity is greater than 0
	                .build()
	        ).toList();
	}

				
				
				
	
}
