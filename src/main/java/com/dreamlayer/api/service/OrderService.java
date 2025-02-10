package com.dreamlayer.api.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.dreamlayer.api.dto.InventoryResponse;
import com.dreamlayer.api.dto.OrderLineItemsDto;
import com.dreamlayer.api.dto.OrderRequest;
import com.dreamlayer.api.modal.Order;
import com.dreamlayer.api.modal.OrderLineItems;
import com.dreamlayer.api.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest) {
		
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
				.map(this::mapToDto)
				.toList();
		order.setOrderLineItemsList(orderLineItems);
		
		List<String> skuCodes = order.getOrderLineItemsList().stream()
				.map(orderLineItem -> orderLineItem.getSkuCode())
				.toList();
				
		// Call Inventory Service, and place order if product is in stock
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                 .uri("http://inventory-service/api/inventory",
                         uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                 .retrieve()
                 .bodyToMono(InventoryResponse[].class)
                 .block();
		
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
		if(allProductsInStock) {
			orderRepository.save(order);
		}
		else {
			throw new IllegalArgumentException("product is not in stock, please try again");
		}
	}
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		
		OrderLineItems orderLineitems = new OrderLineItems();
		orderLineitems.setPrice(orderLineItemsDto.getPrice());
		orderLineitems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineitems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineitems;
	}
}
