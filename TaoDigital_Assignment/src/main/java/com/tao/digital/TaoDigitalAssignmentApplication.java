package com.tao.digital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan (basePackages =  {"com.tao.digital"})
public class TaoDigitalAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoDigitalAssignmentApplication.class, args);
	}

}
