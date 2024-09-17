package com.pedidosepagamentos.circuitbreacker.fallback;

import java.util.concurrent.TimeoutException;

import com.pedidosepagamentos.circuitbreacker.exception.ErrorHandler;
import com.pedidosepagamentos.circuitbreacker.model.Pedido;
import com.pedidosepagamentos.circuitbreacker.repository.PedidoRepository;

import feign.FeignException;

public class Fallback {

    private final PedidoRepository repository;
    private final ErrorHandler handler;

    public Fallback(PedidoRepository repository, ErrorHandler handler) {
        this.repository = repository;
        this.handler 	= handler;
    }

    public Pedido processarPagamentoFallback(Pedido pedido, Throwable t) {
        pedido.setStatus(
            (t instanceof FeignException feignException) ?
                (feignException.status() >= 500 ? handler.handleTemporaryError(feignException) : handler.handleClientError(feignException))
            : (t instanceof TimeoutException) ? 
                handler.handleTimeout()
            : handler.handleUnknownError(t)
        );

        return repository.save(pedido);
    }
}
