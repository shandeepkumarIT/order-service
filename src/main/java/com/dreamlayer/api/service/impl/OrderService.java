package com.dreamlayer.api.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamlayer.api.dto.CommonResponse;
import com.dreamlayer.api.dto.InventoryResponse;
import com.dreamlayer.api.dto.OrderLineItemsDto;
import com.dreamlayer.api.dto.OrderRequest;
import com.dreamlayer.api.manage.IOrderManage;
import com.dreamlayer.api.modal.Order;
import com.dreamlayer.api.modal.OrderLineItems;
import com.dreamlayer.api.service.IMethodUtils;
import com.dreamlayer.api.service.IOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OrderService implements IOrderService{
	
	@Autowired
	private IOrderManage iOrderManage;
	
	@Autowired
	private IMethodUtils methodUtils;
	
	public CommonResponse placeOrder(OrderRequest orderRequest) {
		
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
		List<InventoryResponse> result = iOrderManage.placeOrder(skuCodes);
		if(result.size() > 0) {
			
			InventoryResponse[] content = (InventoryResponse[]) result.toArray();
			boolean allProductsInStock = Arrays.stream(content).allMatch(InventoryResponse::isInStock);
			if(allProductsInStock) {
				
				iOrderManage.saveOrderData(order);
				commonResponse.setStatus_code(00);
				commonResponse.setStatus("Success");
				commonResponse.setTitle("Place Order");
				commonResponse.setMessage(methodUtils.getLocalizedMessage("successOrder"));
			}
			else {
				commonResponse.setStatus_code(01);
				commonResponse.setStatus("Failure");
				commonResponse.setTitle("Place Order");
				commonResponse.setMessage(methodUtils.getLocalizedMessage("inValidStock"));
			}
		}
		else {
			commonResponse.setStatus_code(01);
			commonResponse.setStatus("Failure");
			commonResponse.setTitle("Place Order");
			commonResponse.setMessage(methodUtils.getLocalizedMessage("circuitOrder"));
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
}
