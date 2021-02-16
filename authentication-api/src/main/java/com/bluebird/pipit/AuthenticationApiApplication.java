package com.bluebird.pipit;

import com.bluebird.pipit.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bluebird.pipit.portal.ConnectionProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConnectionProperties.class)
@EnableConfigurationProperties(AppProperties.class)
public class AuthenticationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApiApplication.class, args);
	}
}
