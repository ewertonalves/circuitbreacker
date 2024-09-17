package com.pedidosepagamentos.circuitbreacker.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pedidosepagamentos.circuitbreacker.config.RabbitConfig;
import com.pedidosepagamentos.circuitbreacker.model.Pedido;

@Service
public class PedidoConsumerRabbitService {

    private final PedidoService pedidoService;

    public PedidoConsumerRabbitService(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * TODO Recebe os dados da fila enviados pelo RabbitTemplate
     * */
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    @Transactional
    public void processarPagamento(Pedido pedido) {
        try {
            pedidoService.criarPedido(pedido);
        } catch (Exception e) {
            System.err.println("Erro ao processar pagamento: " + e.getMessage());
        }
    }
}
