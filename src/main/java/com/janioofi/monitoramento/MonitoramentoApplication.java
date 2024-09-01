package com.janioofi.monitoramento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitoramentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoramentoApplication.class, args);
	}

}
