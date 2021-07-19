package com.bluebird.pipit.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.boot.starter.autoconfigure.SwaggerUiWebMvcConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

@Configuration
@ConditionalOnProperty(
	value = "springfox.documentation.enabled",
	havingValue = "true",
	matchIfMissing = true
)
@Import({
	BeanValidatorPluginsConfiguration.class,
	Swagger2DocumentationConfiguration.class,
	SwaggerUiWebMvcConfiguration.class
})
@AutoConfigureAfter({
	WebMvcAutoConfiguration.class,
	JacksonAutoConfiguration.class,
	HttpMessageConvertersAutoConfiguration.class
})
public class SwaggerConfig {
	@Bean
	Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.any())
			.paths(PathSelectors.any())
			.build()
			.useDefaultResponseMessages(false);
	}
}
