package com.pedidosepagamentos.circuitbreacker.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.pedidosepagamentos.circuitbreacker.PagamentoFeignClient.PagamentoClient;
import com.pedidosepagamentos.circuitbreacker.config.RabbitConfig;
import com.pedidosepagamentos.circuitbreacker.enums.StatusEnum;
import com.pedidosepagamentos.circuitbreacker.model.Pedido;
import com.pedidosepagamentos.circuitbreacker.model.request.PagamentoRequest;
import com.pedidosepagamentos.circuitbreacker.model.response.PagamentoResponse;
import com.pedidosepagamentos.circuitbreacker.repository.PedidoRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

	private final PedidoRepository repository;
	private final PagamentoClient pagamentoClient;
	private final RabbitTemplate rabbitTemplate;

	public PedidoService(PedidoRepository repository, PagamentoClient pagamentoClient, RabbitTemplate rabbitTemplate) {
		this.repository  	 = repository;
		this.pagamentoClient = pagamentoClient;
		this.rabbitTemplate  = rabbitTemplate;
	}

	@Transactional
	@CircuitBreaker(name = "pagamentoClient", fallbackMethod = "processarPagamentoFallback")
	public Pedido criarPedido(Pedido pedido) {
        
		pedido.setStatus(StatusEnum.PEDENTE);
		
		Pedido pedidoCriado 				= repository.save(pedido);
		PagamentoRequest pagamentoRequest 	= new PagamentoRequest(pedidoCriado.getId(), pedidoCriado.getValor());
		PagamentoResponse pagamentoResponse = pagamentoClient.processarPagamento(pagamentoRequest);
		
		pedidoCriado.setStatus(pagamentoResponse.isSucesso() ? StatusEnum.PAGO : StatusEnum.CANCELADO);
	    rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, pedidoCriado);

		return repository.save(pedidoCriado);
	}
	
	public Pedido fallbackProcessarPagamento(Pedido pedido, Throwable t) {
		 pedido.setStatus(StatusEnum.CANCELADO);
	     return repository.save(pedido);
	}
	
	@Transactional
	public void atualizarStatus(Long id, String status) {
		Pedido pedido = repository.findById(id).orElse(null);
		pedido.setStatus(StatusEnum.PAGO);
		repository.save(pedido);
	}

	@Transactional
	public List<Pedido> listarPedidos() {
		return repository.findAll();
	}

	@Transactional
	public void cancelarPedido(Long id, String status) {
		Pedido pedido = repository.findById(id).orElse(null);
		pedido.setStatus(StatusEnum.CANCELADO);
		repository.save(pedido);
	}
}
