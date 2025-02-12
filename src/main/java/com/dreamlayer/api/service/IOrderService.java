package com.dreamlayer.api.service;

import com.dreamlayer.api.dto.CommonResponse;
import com.dreamlayer.api.dto.OrderRequest;

public interface IOrderService {

	public CommonResponse placeOrder(OrderRequest orderRequest);
}
