package com.example.dreamauto;

import com.example.dreamauto.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class DreamautoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamautoApplication.class, args);
	}

}
