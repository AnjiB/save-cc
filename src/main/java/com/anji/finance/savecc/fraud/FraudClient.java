package com.anji.finance.savecc.fraud;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FraudClient implements IFraudClient {

	@Value("${ccfc.url}")
	private String ccFcUrl;
	
	private static final String PATH = "/v1/fraudcheck/score";

	@Override
	public CCFraudInfo getFraudInfo(String ccNo, LocalDate expDate) {
		
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(ccFcUrl + PATH)
		        .queryParam("cc", ccNo)
		        .queryParam("exp", expDate.toString())
		        .encode()
		        .toUriString();


		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> params = new HashMap<>();
		params.put("cc", ccNo);
		params.put("exp", expDate.toString());

		CCFraudInfo result = restTemplate.getForObject(urlTemplate, CCFraudInfo.class);

		return result;
	}

}
