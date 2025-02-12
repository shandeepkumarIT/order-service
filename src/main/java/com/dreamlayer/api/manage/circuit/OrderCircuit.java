package com.dreamlayer.api.manage.circuit;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dreamlayer.api.dto.InventoryResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderCircuit {
	
	public InventoryResponse[] placeOrderFallBack(List<String> skuCodes, Exception e) {
		
		log.info("Inventory Service is down...");
		InventoryResponse[] result = {};
		return result;
	}
}
