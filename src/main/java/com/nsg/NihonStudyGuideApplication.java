package com.nsg;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SecurityScheme(
		name = "Authorization",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer"
)
@OpenAPIDefinition(
		info = @Info(
				title = "Nihon Study Guide",
				version = "V.1.0",
				description = "Documentation for Nihon Management apis"
		)
)
@SpringBootApplication
public class NihonStudyGuideApplication {
	public static void main(String[] args) {
		SpringApplication.run(NihonStudyGuideApplication.class, args);
	}
}
