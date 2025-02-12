package com.dreamlayer.api.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamlayer.api.dto.CommonResponse;
import com.dreamlayer.api.dto.InventoryResponse;
import com.dreamlayer.api.dto.OrderLineItemsDto;
import com.dreamlayer.api.dto.OrderRequest;
import com.dreamlayer.api.manage.OrderManage;
import com.dreamlayer.api.modal.Order;
import com.dreamlayer.api.modal.OrderLineItems;
import com.dreamlayer.api.repository.OrderRepository;
import com.dreamlayer.api.service.IOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService implements IOrderService{
	
	private final MessageSource messageSource;
	
	private final OrderRepository orderRepository;
	private final OrderManage orderManage;
	
	public CommonResponse placeOrder(OrderRequest orderRequest) {
		
		Locale locale = LocaleContextHolder.getLocale();
		CommonResponse commonResponse = new CommonResponse();
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
		InventoryResponse[] result = orderManage.placeOrder(skuCodes);
		if(result.length > 0) {
			
			boolean allProductsInStock = Arrays.stream(result).allMatch(InventoryResponse::isInStock);
			if(allProductsInStock) {
				
				orderRepository.save(order);
				commonResponse.setStatus_code(00);
				commonResponse.setStatus("Success");
				commonResponse.setTitle("Place Order");
				commonResponse.setMessage(getLocalizedMessage("successOrder"));
			}
			else {
				commonResponse.setStatus_code(01);
				commonResponse.setStatus("Failure");
				commonResponse.setTitle("Place Order");
				commonResponse.setMessage(getLocalizedMessage("inValidStock"));
			}
		}
		else {
			commonResponse.setStatus_code(01);
			commonResponse.setStatus("Failure");
			commonResponse.setTitle("Place Order");
			commonResponse.setMessage(getLocalizedMessage("circuitOrder"));
		}
		return commonResponse;
	}
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		
		OrderLineItems orderLineitems = new OrderLineItems();
		orderLineitems.setPrice(orderLineItemsDto.getPrice());
		orderLineitems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineitems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineitems;
	}
	
	private String getLocalizedMessage(String translationKey) {
		
		Locale locale = LocaleContextHolder.getLocale();
		log.info("lang :: " + locale);
		return messageSource.getMessage(translationKey, null, locale);
	}
}
