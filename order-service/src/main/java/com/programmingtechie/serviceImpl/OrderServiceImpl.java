 package com.programmingtechie.serviceImpl;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.programmingtechie.dto.InventoryResponse;
import com.programmingtechie.dto.OrderLineItemsDto;
import com.programmingtechie.dto.OrderRequestDto;
import com.programmingtechie.entity.Order;
import com.programmingtechie.entity.OrderLineItems;
import com.programmingtechie.event.OrderPlacedEvent;
import com.programmingtechie.repository.OrderRepository;
import com.programmingtechie.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	

	@Autowired
	private WebClient webClient; // WebClient is correctly autowired

	 @Autowired
	 private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate; // KafkaTemplate with parameterized types

	@Override
	public String placeOrder(OrderRequestDto orderRequestDto) {
		// Create a new order
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());

		// Map the OrderLineItemsDto to OrderLineItems entities
		List<OrderLineItems> orderLineItemsList = orderRequestDto.getOrderLineItemsListDto().stream()
				.map(this::mapToOrderLineItemEntity).collect(Collectors.toList());

		// Set the order line items in the order
		order.setOrderLineItemsList(orderLineItemsList);

		// Extract the SKU codes from the order items
		List<String> skuCodes = orderLineItemsList.stream().map(OrderLineItems::getSkuCode)
				.collect(Collectors.toList());

		// Call the inventory service to check stock availability (synchronously)
		InventoryResponse[] inventoryResponseArray = webClient.get()
				.uri("http://localhost:8082/api/inventory/isInStock",
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes) // Pass SKU codes as query parameter
								.build())
				.retrieve().bodyToMono(InventoryResponse[].class) // Expect an array of InventoryResponse
				.block(); // Blocking call to wait for the response

		// Check if all SKUs are in stock
		boolean allProductInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

		// If any product is out of stock, throw an exception
		if (allProductInStock) {

			orderRepository.save(order);
			kafkaTemplate.send("notification",new OrderPlacedEvent(order.getOrderNumber()));
			return "Order Placed Successfully";
		} else {
			throw new IllegalArgumentException("One or more products are not in stock.");
		}

	}

	// Helper method to map OrderLineItemsDto to OrderLineItems entity
	public OrderLineItems mapToOrderLineItemEntity(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

		return orderLineItems;
	}
}
