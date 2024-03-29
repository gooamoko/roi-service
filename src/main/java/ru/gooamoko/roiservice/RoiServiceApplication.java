package ru.gooamoko.roiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "ru.gooamoko.roiservice.config.properties")
public class RoiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoiServiceApplication.class, args);
	}
}
