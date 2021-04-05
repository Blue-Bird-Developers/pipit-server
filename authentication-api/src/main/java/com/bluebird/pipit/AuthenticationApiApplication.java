package com.bluebird.pipit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.bluebird.pipit.portal.ConnectionProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConnectionProperties.class)
public class AuthenticationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationApiApplication.class, args);
	}
}
