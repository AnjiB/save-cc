package com.anji.finance.savecc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.anji.finance.savecc.fraud.CCFraudInfo;
import com.simple.api.core.contract.ApiResponse;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;


@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName="fraud-service-provider")
public class SaveCcPactConsumerTest {
	
	private static final String SAVE_CREDIT_CARD_CONSUMER = "save-credit-card-consumer";


	@Pact(consumer= SAVE_CREDIT_CARD_CONSUMER)
	public RequestResponsePact getScoreForValidCreditCard(PactDslWithProvider builder) {
		
		String responseJson = "{\n"
				+ "  \"ccNumber\": \"4532788397355156\",\n"
				+ "  \"issueDate\": \"2019-03-31\",\n"
				+ "  \"expDate\": \"2023-03-31\",\n"
				+ "  \"fraudProps\": {\n"
				+ "    \"issuer\": \"VISA\",\n"
				+ "    \"fraudScore\": 700\n"
				+ "  }\n"
				+ "}";
		
		
		return builder.given("Valid credit card is provided")
				.uponReceiving("Fraud score for a valid credit card")
				.path("/v1/fraudcheck/score")
				.encodedQuery("cc=4532788397355156&exp=2023-03-31")
				.headers(Map.of("Authorization", "Basic YW5qaWFkbWluOmFnb29kcWFwZXJzb24="))
				.method("GET")
				.willRespondWith()
				.status(200)
				.body(responseJson)
				.toPact();
		
	}
	
	
	@Pact(consumer= SAVE_CREDIT_CARD_CONSUMER)
	public RequestResponsePact getErrorMessageForInvalidCreditCard(PactDslWithProvider builder) {
		
		String responseJson = "{\n"
				+ "  \"error\": {\n"
				+ "    \"message\": \"There is no credit with given card number. Please enter valid credit card\"\n"
				+ "  }\n"
				+ "}";
		
		
		return builder.given("Invalid credit card is provided")
				.uponReceiving("Error message from ccfc service")
				.path("/v1/fraudcheck/score")
				.encodedQuery("cc=test&exp=2023-03-31")
				.method("GET")
				.willRespondWith()
				.status(500)
				.body(responseJson)
				.toPact();
		
	}
	
	
	
	@Test
	@PactTestFor(pactMethod = "getScoreForValidCreditCard")
	public void testFraudScoreForValidCreditCard(MockServer server) throws Exception {
	
		FraudTestClient ftc = new FraudTestClient(server.getUrl());
		ApiResponse response = ftc.getFraudScoreForValidCreditCard("4532788397355156", "2023-03-31", Map.of("Authorization", "Basic YW5qaWFkbWluOmFnb29kcWFwZXJzb24="));
		assertThat(response.getStatusCode()).isEqualTo(200);
		var body = response.getResponseBodyAs(CCFraudInfo.class);
		assertThat(body.getCcNumber()).isEqualTo("4532788397355156");
		assertThat(body.getIssueDate()).isEqualTo("2019-03-31");
		assertThat(body.getExpDate()).isEqualTo("2023-03-31");
		assertThat(body.getFraudProps().getIssuer()).isEqualTo("VISA");
		assertThat(body.getFraudProps().getFraudScore()).isEqualTo(700);
		
	}
	
	
	@Test
	@PactTestFor(pactMethod = "getErrorMessageForInvalidCreditCard")
	public void testErroMessageForValidCreditCard(MockServer server) throws Exception {
		
		FraudTestClient ftc = new FraudTestClient(server.getUrl());
		ApiResponse response = ftc.getFraudScoreForValidCreditCard("test", "2023-03-31", Map.of("Authorization", "Basic YW5qaWFkbWluOmFnb29kcWFwZXJzb24="));
		assertThat(response.getStatusCode()).isEqualTo(500);
		var body = response.getResponseBodyAs(CCFraudInfo.class);
		assertThat(body.getError().getMessage()).isEqualTo("There is no credit with given card number. Please enter valid credit card");
	}
	
}
