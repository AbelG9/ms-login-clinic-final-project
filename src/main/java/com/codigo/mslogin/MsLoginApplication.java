package com.codigo.mslogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.codigo.*")
@EnableFeignClients("com.codigo.mslogin")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class MsLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLoginApplication.class, args);
	}

}
