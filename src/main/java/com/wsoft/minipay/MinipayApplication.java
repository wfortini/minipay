package com.wsoft.minipay;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.kafka.config.TopicBuilder;

@EnableJdbcAuditing
@SpringBootApplication
public class MinipayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinipayApplication.class, args);
	}

	@Bean
	public NewTopic notificationTopic() {
		return TopicBuilder.name("transaction-notification")
				.build();
	}

}
