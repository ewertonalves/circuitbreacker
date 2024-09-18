## **Documentação do Projeto: Sistema de Pedidos e Pagamentos com Circuit Breaker**

### **Visão Geral**

Este projeto é um sistema de processamento de pedidos e pagamentos que utiliza **Java 21**, **Spring Boot**, **Resilience4j**, **RabbitMQ**, **FeignClient** e uma **base de dados H2**. O sistema implementa um **Circuit Breaker** para lidar com falhas no serviço de pagamentos e realiza a comunicação entre serviços através de filas RabbitMQ.

### **Arquitetura**

* **Spring Boot**: Framework principal usado para construir o sistema.  
* **Resilience4j**: Gerencia o Circuit Breaker para garantir resiliência em chamadas externas.  
* **RabbitMQ**: Usado para comunicação assíncrona entre os componentes.  
* **FeignClient**: Usado para integrar o sistema com o serviço de pagamentos.  
* **H2**: Banco de dados em memória para persistência temporária de dados.

### **Componentes Principais**

#### **1\. Configuração do RabbitMQ**

As classes relacionadas ao RabbitMQ são responsáveis pela configuração da fila, do exchange e da conexão com o RabbitMQ.

* **`RabbitConfig`**: Define a configuração de fila (`pedidoQueue`), exchange (`pedidoExchange`) e a chave de roteamento (`pedidoRoutingKey`).  
* **`RabbitMQConfigConection`**: Configura a conexão com o RabbitMQ, utilizando um `CachingConnectionFactory` para gerenciar a conexão de maneira eficiente.  
* **`RabbitMQConnectionChecker`**: Classe de serviço que verifica se a conexão com o RabbitMQ está ativa.

#### **2\. PedidoService**

A classe `PedidoService` gerencia a lógica de negócios relacionada aos pedidos:

* **Método `criarPedido`**: Cria um pedido, realiza a chamada ao serviço de pagamentos usando **FeignClient** e publica o pedido na fila RabbitMQ.  
* **Circuit Breaker**: O método `criarPedido` utiliza a anotação `@CircuitBreaker` para implementar o padrão de Circuit Breaker, especificando o método de fallback (`processarPagamentoFallback`) para lidar com falhas nas chamadas ao serviço de pagamento.  
* **Outros métodos**: `atualizarStatus`, `listarPedidos` e `cancelarPedido`, que permitem modificar e recuperar pedidos do repositório.

#### **3\. Fallback**

A classe `Fallback` fornece uma lógica de fallback para quando ocorrem erros nas chamadas externas ao serviço de pagamento.

* **`processarPagamentoFallback`**: Define diferentes comportamentos dependendo do tipo de exceção, como `FeignException` ou `TimeoutException`.

#### **4\. ErrorHandler**

A classe `ErrorHandler` centraliza o tratamento de erros relacionados ao pagamento:

* **`handleTemporaryError`**: Lida com erros temporários no serviço de pagamento.  
* **`handleClientError`**: Trata erros causados por problemas no cliente de pagamento.  
* **`handleTimeout`**: Lida com problemas de timeout ao processar um pagamento.  
* **`handleUnknownError`**: Trata erros inesperados.

#### **5\. FeignClient (PagamentoClient)**

A interface `PagamentoClient` é responsável por comunicar-se com o serviço de pagamentos, utilizando Feign para realizar chamadas HTTP.

* O método `processarPagamento` envia um pedido de pagamento à API externa.

#### **6\. Enums (StatusEnum)**

A enumeração `StatusEnum` define os diferentes estados de um pedido:

* **PEDENTE**: Pedido aguardando pagamento.  
* **PAGO**: Pedido pago com sucesso.  
* **CANCELADO**: Pedido cancelado devido a falha no pagamento ou erro inesperado.

### **Configurações Externas**

#### **1\. Configurações de RabbitMQ**

* **Host**: `localhost`  
* **Porta**: `5672`  
* **Credenciais**: `guest/guest`

#### **2\. Configurações de Banco de Dados**

* **URL**: `jdbc:h2:mem:testdb`  
* **Usuário**: `sa`  
* **Senha**: `password`

#### **3\. Configurações do Circuit Breaker (Resilience4j)**

* **Ativação do Health Indicator**: Verifica a saúde do Circuit Breaker.  
* **Número de Chamadas Permitidas em Half-Open**: 5\.  
* **Tamanho da Janela Deslizante**: 10\.  
* **Taxa de Falha Permitida**: 50%.  
* **Tempo de Espera no Estado Open**: 60 segundos.  
* **Limiar de Duração de Chamadas Lentas**: 5 segundos.

### **Fluxo do Sistema**

1. **Criação de Pedido**: Um pedido é criado e salvo no banco de dados. O status inicial do pedido é **PEDENTE**.  
2. **Processamento de Pagamento**: O sistema faz uma chamada ao serviço de pagamento usando o **FeignClient**. Se o pagamento for bem-sucedido, o status do pedido é alterado para **PAGO**; caso contrário, o pedido é **CANCELADO**.  
3. **Publicação na Fila RabbitMQ**: Após o processamento, o pedido é enviado para uma fila RabbitMQ, onde outros componentes do sistema podem consumi-lo.  
4. **Fallback**: Se o serviço de pagamento estiver indisponível, o **Circuit Breaker** entra em ação, acionando o método de fallback que define o status do pedido como **CANCELADO**.

### **Tecnologias Utilizadas**

* **Java 21**  
* **Spring Boot**  
* **RabbitMQ**  
* **Feign**  
* **Resilience4j** (Circuit Breaker)  
* **H2 Database**

### **Potenciais Expansões**

* Implementar um sistema de notificação para avisar os clientes sobre o status de seus pedidos.  
* Adicionar logs e métricas mais detalhadas para monitorar o comportamento do Circuit Breaker.  
* Melhorar a estratégia de retry para o serviço de pagamento antes de acionar o Circuit Breaker.
