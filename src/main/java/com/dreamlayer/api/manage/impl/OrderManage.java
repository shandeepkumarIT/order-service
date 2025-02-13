package com.dreamlayer.api.manage.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamlayer.api.dto.CommonResponse;
import com.dreamlayer.api.dto.InventoryResponse;
import com.dreamlayer.api.manage.IOrderManage;
import com.dreamlayer.api.modal.Order;
import com.dreamlayer.api.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

import static com.dreamlayer.api.utils.ConstantUtils.QueryParamMappings.SKUCODE;

@Slf4j
@Component
public class OrderManage implements IOrderManage {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Value("${external.inventory-service}")
	private String inventoryApi;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InventoryResponse> placeOrder(List<String> skuCodes) {
		
		List<?> inventoryResponseArray = null;
		try {
			
			CommonResponse result = webClientBuilder.build().get()
	                .uri(inventoryApi, uriBuilder -> uriBuilder.queryParam(SKUCODE, skuCodes).build())
	                .retrieve()
	                .bodyToMono(CommonResponse.class)
	                .block();
			log.info("Shandeep Kumar :: " + result.getTitle());
			inventoryResponseArray = result.getResponse();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (List<InventoryResponse>) inventoryResponseArray;
	}
	
	public void saveOrderData(Order order) {
		
		orderRepository.save(order);
	}
}
