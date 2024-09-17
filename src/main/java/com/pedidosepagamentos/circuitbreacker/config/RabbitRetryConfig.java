package com.pedidosepagamentos.circuitbreacker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitRetryConfig {

	@Bean
	public Queue retryQueue() {
		return QueueBuilder.durable("retryQueue").withArgument("x-message-ttl", 60000) // Tempo de vida das mensagens
				.withArgument("x-dead-letter-exchange", "dlxExchange").build();
	}

	@Bean
	public TopicExchange dlxExchange() {
		return new TopicExchange("dlxExchange");
	}

	@Bean
	public Queue dlq() {
		return new Queue("dlq", true);
	}

	@Bean
	public Binding dlqBinding() {
		return BindingBuilder.bind(dlq()).to(dlxExchange()).with("retryRoutingKey");
	}

	@Bean
	public Binding retryBinding() {
		return BindingBuilder.bind(retryQueue()).to(new TopicExchange("mainExchange")).with("retryRoutingKey");
	}
}
