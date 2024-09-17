package com.pedidosepagamentos.circuitbreacker.PagamentoFeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pedidosepagamentos.circuitbreacker.model.request.PagamentoRequest;
import com.pedidosepagamentos.circuitbreacker.model.response.PagamentoResponse;

@FeignClient(name = "pagamentoClient", url = "http://pagamentos-api.com")
public interface PagamentoClient {
	
	@PostMapping("api/v1/pagamentos/processar")
	PagamentoResponse processarPagamento(@RequestBody PagamentoRequest request);

}
