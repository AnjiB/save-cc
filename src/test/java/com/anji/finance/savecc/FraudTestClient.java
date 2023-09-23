package com.anji.finance.savecc;

import java.util.HashMap;
import java.util.Map;

import com.simple.api.core.contract.ApiResponse;
import com.simple.api.core.enums.RequestMethod;
import com.simple.api.core.impl.BaseApiClient;
import com.simple.api.core.modal.RestRequest;

import io.restassured.http.ContentType;


/**
 * @author anjiboddupally
 *
 */


public class FraudTestClient extends BaseApiClient {

	public FraudTestClient(String baseURL) {
		super(baseURL);
	}
	
	// Pass LocalDate instead of String
	public ApiResponse getFraudScoreForValidCreditCard(String creditCard, String expDate, Map<String, String> heads) throws Exception {
		
		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("cc", creditCard);
		queryMap.put("exp", expDate);
		
		
		RestRequest request = RestRequest.builder()
				.method(RequestMethod.GET)
				.contentType(ContentType.JSON)
				.headers(heads)
				.path("/v1/fraudcheck/score")
				.queryParams(queryMap)
				.build();
		
		return send(request);
	}
}
