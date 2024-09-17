package com.pedidosepagamentos.circuitbreacker.model.request;

import java.math.BigDecimal;

public class PagamentoRequest {

	private Long pedidoId;
	private BigDecimal valor;

	public PagamentoRequest(Long pedidoId, BigDecimal valor) {
		this.pedidoId = pedidoId;
		this.valor = valor;
	}

	public PagamentoRequest() {

	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "PagamentoRequest [pedidoId=" + pedidoId + ", valor=" + valor + "]";
	}

}
