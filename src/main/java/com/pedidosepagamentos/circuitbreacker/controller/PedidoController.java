package com.pedidosepagamentos.circuitbreacker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedidosepagamentos.circuitbreacker.enums.StatusEnum;
import com.pedidosepagamentos.circuitbreacker.model.MessageResponse;
import com.pedidosepagamentos.circuitbreacker.model.Pedido;
import com.pedidosepagamentos.circuitbreacker.service.PedidoService;

@RestController
@RequestMapping("api/v1/pedidos")
public class PedidoController {

	private final PedidoService service;

	public PedidoController(PedidoService service) {
		this.service = service;
	}

	@PostMapping(value = "/criar", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MessageResponse> criarPedido(@RequestBody Pedido pedido) {
	    try {
	        Pedido pedidoCriado = service.criarPedido(pedido);

	        String mensagem = pedidoCriado.getStatus() == StatusEnum.PAGO 
	            ? "Pedido realizado e pagamento aprovado"
	            : "Pedido realizado, mas pagamento falhou";

	        return new ResponseEntity<>(new MessageResponse(mensagem), HttpStatus.CREATED);
	    } catch (Exception e) {
	        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping(value = "/listar", consumes = "application/json; charset=UTF-8")
	public ResponseEntity<List<Pedido>> listarPedidos() {
		return new ResponseEntity<List<Pedido>>(service.listarPedidos(), HttpStatus.OK);
	}

	@PutMapping(value = "/atualizarStatus", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MessageResponse> atualizarPedido(@PathVariable Long id, @RequestBody String status) {
		try {
			service.atualizarStatus(id, status);
			return new ResponseEntity<MessageResponse>(new MessageResponse("Status do pedido atualizado"),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<MessageResponse>(new MessageResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/cancelar", produces = "application/json; charset=UTF-8")
	public ResponseEntity<MessageResponse> cancelarPedido(@PathVariable Long id, @RequestBody String status) {
		try {
			service.cancelarPedido(id, status);
			return new ResponseEntity<MessageResponse>(new MessageResponse("Status do pedido atualizado"),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<MessageResponse>(new MessageResponse(e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
