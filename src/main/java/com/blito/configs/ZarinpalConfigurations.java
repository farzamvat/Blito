package com.blito.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.blito.services.ZarinpalClient;

@Configuration
public class ZarinpalConfigurations {

	@Value("zarinpal.url")
	private String zarinpalURL;
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath("com.blito.payments.zarinpal");
		return marshaller;
	}
	
	@Bean
	public ZarinpalClient zarinpalClient(Jaxb2Marshaller marshaller)
	{
		ZarinpalClient client = new ZarinpalClient(zarinpalURL);
		client.setDefaultUri("https://www.zarinpal.com/pg/services/WebGate/service");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
	
}
