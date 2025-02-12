package com.dreamlayer.api.controller;

import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_200_CODE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_200_MESSAGE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_400_CODE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_400_MESSAGE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_401_CODE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_401_MESSAGE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_403_CODE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_403_MESSAGE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_404_CODE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_404_MESSAGE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_429_CODE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_429_MESSAGE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_500_CODE;
import static com.dreamlayer.api.utils.Constants.HttpCodes.HTTP_500_MESSAGE;
import static com.dreamlayer.api.utils.Constants.RequestMappings.ORDER;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.dreamlayer.api.dto.CommonResponse;
import com.dreamlayer.api.dto.OrderRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@Tag(name = ORDER, description = "ORDER details display")
public interface IOrderController {

	@Operation(summary = "Place the Order")
	@ApiResponses({
		@ApiResponse(responseCode = HTTP_200_CODE, description = HTTP_200_MESSAGE, content = { @Content(schema = @Schema(implementation = CommonResponse.class), mediaType=APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = HTTP_400_CODE, description = HTTP_400_MESSAGE),
		@ApiResponse(responseCode = HTTP_401_CODE, description = HTTP_401_MESSAGE),
		@ApiResponse(responseCode = HTTP_403_CODE, description = HTTP_403_MESSAGE),
		@ApiResponse(responseCode = HTTP_404_CODE, description = HTTP_404_MESSAGE),
		@ApiResponse(responseCode = HTTP_429_CODE, description = HTTP_429_MESSAGE),
		@ApiResponse(responseCode = HTTP_500_CODE, description = HTTP_500_MESSAGE)
	})
	public ResponseEntity<CommonResponse> placeOrder(@RequestBody OrderRequest orderRequest);
}
