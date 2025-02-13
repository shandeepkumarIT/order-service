package com.dreamlayer.api.manage;

import static com.dreamlayer.api.utils.ConstantUtils.CircuitBreakerMappings.ORDERCIRCUIT;
import static com.dreamlayer.api.utils.ConstantUtils.CircuitBreakerMappings.PLACEORDERFALLBACK;

import java.util.List;

import com.dreamlayer.api.dto.InventoryResponse;
import com.dreamlayer.api.modal.Order;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

public interface IOrderManage {

	// @CircuitBreaker(name = ORDERCIRCUIT, fallbackMethod = PLACEORDERFALLBACK)
	public List<InventoryResponse> placeOrder(List<String> skuCodes);
	
	public void saveOrderData(Order order);
}
