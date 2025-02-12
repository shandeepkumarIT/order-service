package com.dreamlayer.api.utils;

import com.dreamlayer.api.utils.Constants.Global;

public class ConstantUtils {

	private ConstantUtils() {
        throw new IllegalStateException();
    }
	
	public static final class CircuitBreakerMappings {
		
		public static final String ORDERCIRCUIT = "orderCircuit";
		public static final String PLACEORDERFALLBACK = "placeOrderFallBack";
		private CircuitBreakerMappings() {
            throw new IllegalStateException(Global.UTILITY_CLASS);
        }
	}
	
	public static final class QueryParamMappings {
		
		public static final String SKUCODE = "skuCode";
		private QueryParamMappings() {
            throw new IllegalStateException(Global.UTILITY_CLASS);
        }
	}
}
