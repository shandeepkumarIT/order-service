package com.dreamlayer.api.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dreamlayer.api.dto.CommonResponse;
import com.dreamlayer.api.dto.OrderRequest;
import com.dreamlayer.api.service.IOrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.dreamlayer.api.utils.Constants.RequestMappings.ORDER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ORDER)
public class OrderController {

	private final IOrderService orderService;
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
		
		return ResponseEntity.ok(orderService.placeOrder(orderRequest));
	}
}
