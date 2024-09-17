package com.pedidosepagamentos.circuitbreacker.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConnectionChecker {

    private final CachingConnectionFactory connectionFactory;

    public RabbitMQConnectionChecker(CachingConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public boolean isConnected() {
        try {
            connectionFactory.createConnection();
            return true;
        } catch (Exception e) {
        	System.err.println("Erro ao conectar ao RabbitMQ: {}" + e.getMessage());
            return false;
        }
    }
}