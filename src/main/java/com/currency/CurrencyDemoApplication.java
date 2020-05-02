package com.currency;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class CurrencyDemoApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(CurrencyDemoApplication.class);
	public static void main(String[] args) {
		logger.info("In Main SpringBoot class ->CurrencyDemoApplication");
		SpringApplication.run(CurrencyDemoApplication.class, args);
	}

}
