package com.programmingtechie.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.dto.InventoryResponse;
import com.programmingtechie.service.InventoryService;



@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@GetMapping("/isInStock")
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) throws InterruptedException {
		return inventoryService.isInStock(skuCode);
	
		
	}

}
