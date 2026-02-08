package org.serjlemast.rpc;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.serjlemast.dto.ItemResponse;
import org.serjlemast.dto.ItemRequest;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemsRpcClient {
  private final RabbitTemplate rabbitTemplate;

  public ItemResponse fetchItems(ItemRequest request) {
    var correlationId = UUID.randomUUID().toString();
    log.info("CorrelationId: {}", correlationId);
    log.info("Request: {}", request);

      return rabbitTemplate.convertSendAndReceiveAsType(
              request,
              buildMessagePostProcessor(correlationId),
              new ParameterizedTypeReference<ItemResponse>() {});
  }

  private MessagePostProcessor buildMessagePostProcessor(String correlationId) {
    return message -> {
      message.getMessageProperties().setCorrelationId(correlationId);
      message.getMessageProperties().setHeader("X-Correlation-Id", correlationId);
      message.getMessageProperties().setHeader(AmqpHeaders.CORRELATION_ID, correlationId);
      return message;
    };
  }
}
