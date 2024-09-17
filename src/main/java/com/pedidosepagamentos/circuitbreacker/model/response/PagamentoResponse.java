package com.pedidosepagamentos.circuitbreacker.model.response;

public class PagamentoResponse {

	private boolean sucesso;
	private String mensagem;

	public PagamentoResponse() {

	}

	public boolean isSucesso() {
		return sucesso;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	@Override
	public String toString() {
		return "PagamentoResponse [sucesso=" + sucesso + ", mensagem=" + mensagem + "]";
	}

}
