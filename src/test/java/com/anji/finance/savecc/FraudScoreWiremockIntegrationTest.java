package com.anji.finance.savecc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.anji.finance.savecc.fraud.CCFraudInfo;
import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.simple.api.core.contract.ApiResponse;

/**
 * @author anjiboddupally
 *
 */

@SpringBootTest
@ContextConfiguration(initializers = { WireMockInitialiser.class })
@TestInstance(Lifecycle.PER_CLASS)
public class FraudScoreWiremockIntegrationTest {

	private static final String SAVE_CREDIT_CARD_CONSUMER = "save-credit-card-consumer";

	private static final String FRAUD_SERVICE_PROVIDER = "fraud-service-provider";

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE = "Content-Type";

	private static final String AUTHORIZATION = "Authorization";
	
	private static final String BASIC_AUTH_TOKEN = "Basic dXNlcjp0ZXN0";

	@Autowired
	WireMockServer wireMockServer;
	
	
	@BeforeAll
	public void setUp() {
		wireMockServer.addMockServiceRequestListener(
				WireMockPactGenerator.builder(FRAUD_SERVICE_PROVIDER, SAVE_CREDIT_CARD_CONSUMER)
				.build());
	}

	@Test
	public void testCreditScoreForValidCreditCard() throws Exception {

		String responseJson = "{\n" + "  \"ccNumber\": \"4532788397355156\",\n" + "  \"issueDate\": \"2019-03-31\",\n"
				+ "  \"expDate\": \"2023-03-31\",\n" + "  \"fraudProps\": {\n" + "    \"issuer\": \"VISA\",\n"
				+ "    \"fraudScore\": 700\n" + "  }\n" + "}";

		wireMockServer.stubFor(WireMock
				.get(WireMock.urlEqualTo("/v1/fraudcheck/score?cc=4532788397355156&exp=2023-03-31"))
				.withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
				.withBasicAuth("user", "test").willReturn(
						aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON).withStatus(200).withBody(responseJson)));

		FraudTestClient ftc = new FraudTestClient(wireMockServer.baseUrl());

		ApiResponse response = ftc.getFraudScoreForValidCreditCard("4532788397355156", "2023-03-31",
				Map.of(AUTHORIZATION, BASIC_AUTH_TOKEN));
		assertThat(response.getStatusCode()).isEqualTo(200);
		var body = response.getResponseBodyAs(CCFraudInfo.class);
		assertThat(body.getCcNumber()).isEqualTo("4532788397355156");
		assertThat(body.getIssueDate()).isEqualTo("2019-03-31");
		assertThat(body.getExpDate()).isEqualTo("2023-03-31");
		assertThat(body.getFraudProps().getIssuer()).isEqualTo("VISA");
		assertThat(body.getFraudProps().getFraudScore()).isEqualTo(700);

	}

	@Test
	public void testErrorMessageForInValidCreditCard() throws Exception {

		String responseJson = "{\n"
				+ "  \"error\": \"There is no credit with given card number. Please enter valid credit card\"\n" + "}";

		wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/v1/fraudcheck/score?cc=abcd&exp=2023-03-31"))
				.withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
				.withBasicAuth("user", "test").willReturn(
						aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON).withStatus(500).withBody(responseJson)));

		FraudTestClient ftc = new FraudTestClient(wireMockServer.baseUrl());

		ApiResponse response = ftc.getFraudScoreForValidCreditCard("abcd", "2023-03-31",
				Map.of(AUTHORIZATION, BASIC_AUTH_TOKEN));
		assertThat(response.getStatusCode()).isEqualTo(500);
		var body = response.getResponseBodyAs(CCFraudInfo.class);
		assertThat(body.getError())
				.isEqualTo("There is no credit with given card number. Please enter valid credit card");

	}

	@Test
	public void testErrorMessageForInValidCredentials() throws Exception {

		String responseJson = "{\n" + "  \"error\": \"Bad credentials\"\n" + "}";

		wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/v1/fraudcheck/score?cc=abcd&exp=2023-03-31"))
				.withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON))
				.withBasicAuth("user", "test").willReturn(
						aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON).withStatus(401).withBody(responseJson)));

		FraudTestClient ftc = new FraudTestClient(wireMockServer.baseUrl());

		ApiResponse response = ftc.getFraudScoreForValidCreditCard("abcd", "2023-03-31",
				Map.of(AUTHORIZATION, BASIC_AUTH_TOKEN));
		assertThat(response.getStatusCode()).isEqualTo(401);
		var body = response.getResponseBodyAs(CCFraudInfo.class);
		assertThat(body.getError()).isEqualTo("Bad credentials");

	}

}
