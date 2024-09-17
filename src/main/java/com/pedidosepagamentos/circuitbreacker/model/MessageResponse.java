package com.pedidosepagamentos.circuitbreacker.model;

public class MessageResponse {

	private String message;

	public MessageResponse(String string) {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageResponse [message=" + message + "]";
	}

}
