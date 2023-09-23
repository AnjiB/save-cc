package com.anji.finance.savecc;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

/**
 * @author anjiboddupally
 *
 */


public class WireMockInitialiser implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

		WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
		wireMockServer.start();

		configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);

		configurableApplicationContext.addApplicationListener(applicationEvent -> {
			if (applicationEvent instanceof ContextClosedEvent) {
				wireMockServer.stop();
			}
		});
	}
}