package com.programmingtechie.service;

import org.springframework.stereotype.Service;

import com.programmingtechie.dto.ProductRequest;
import com.programmingtechie.entity.Product;
import com.programmingtechie.exception.ProductNotFoundException;


@Service
public interface ProductService {
	
	public void createProdcut(ProductRequest productRequest);
	
	public Product getProductById(Long id) throws ProductNotFoundException;
	
	public void deleteProductById(Long id) throws ProductNotFoundException ;

}
