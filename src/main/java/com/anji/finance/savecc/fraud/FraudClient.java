package com.anji.finance.savecc.fraud;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FraudClient implements IFraudClient {

	@Value("${ccfc.url}")
	private String ccFcUrl;
	
	@Value("${ccfc.username}")
	private String ccFcUsername;
	
	@Value("${ccfc.password}")
	private String ccFcPassword;
	
	private static final String PATH = "/v1/fraudcheck/score";

	@Override
	public CCFraudInfo getFraudInfo(String ccNo, LocalDate expDate) {
		
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(ccFcUrl + PATH)
		        .queryParam("cc", ccNo)
		        .queryParam("exp", expDate.toString())
		        .encode()
		        .toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(ccFcUsername, ccFcPassword);
		
		HttpEntity<?> request = new HttpEntity<>(headers);
		
		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> params = new HashMap<>();
		params.put("cc", ccNo);
		params.put("exp", expDate.toString());
		
		
		ResponseEntity<CCFraudInfo> response = restTemplate.exchange(
				urlTemplate,
		        HttpMethod.GET,
		        request,
		        CCFraudInfo.class);

		//CCFraudInfo result = restTemplate.getForObject(urlTemplate, request, CCFraudInfo.class);

		return response.getBody();
	}

}
