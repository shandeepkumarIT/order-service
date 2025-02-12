package com.dreamlayer.api.manage;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.dreamlayer.api.dto.InventoryResponse;
import com.dreamlayer.api.manage.circuit.OrderCircuit;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.dreamlayer.api.utils.ConstantUtils.CircuitBreakerMappings.ORDERCIRCUIT;
import static com.dreamlayer.api.utils.ConstantUtils.CircuitBreakerMappings.PLACEORDERFALLBACK;
import static com.dreamlayer.api.utils.ConstantUtils.QueryParamMappings.SKUCODE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderManage extends OrderCircuit {

	@Value("${external.inventory-service}")
	private String inventoryApi;
	
	private final WebClient.Builder webClientBuilder;
	
	@CircuitBreaker(name = ORDERCIRCUIT, fallbackMethod = PLACEORDERFALLBACK)
	public InventoryResponse[] placeOrder(List<String> skuCodes) {
		
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri(inventoryApi, uriBuilder -> uriBuilder.queryParam(SKUCODE, skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
		return inventoryResponseArray;
	}
}
