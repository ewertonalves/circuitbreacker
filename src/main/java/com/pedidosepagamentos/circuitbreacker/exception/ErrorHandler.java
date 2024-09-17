package com.pedidosepagamentos.circuitbreacker.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pedidosepagamentos.circuitbreacker.enums.StatusEnum;

import feign.FeignException;

public class ErrorHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
	
	public StatusEnum handleTemporaryError(FeignException e) {
		logger.warn("Erro temporário no serviço de pagamento: {}", e.getMessage());
		return StatusEnum.CANCELADO;
	}
	
	public StatusEnum handleClientError(FeignException e) {
		logger.error("Erro no pagamento. Cancelado pedido: {}", e.getMessage());
		return StatusEnum.CANCELADO;
	}
	
	public StatusEnum handleTimeout() {
        logger.error("Timeout ao processar pagamento. Cancelando pedido.");
        return StatusEnum.CANCELADO;
    }
	
    public StatusEnum handleUnknownError(Throwable t) {
        logger.error("Erro inesperado ao processar pagamento: {}", t.getMessage());
        return StatusEnum.CANCELADO;
    }

}
