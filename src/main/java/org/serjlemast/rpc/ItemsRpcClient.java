package org.serjlemast.rpc;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.serjlemast.dto.ItemResponse;
import org.serjlemast.dto.ItemsRequest;
import org.serjlemast.model.Item;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemsRpcClient {
  private final RabbitTemplate rabbitTemplate;

  public ItemResponse fetchItems() {
    log.info("Fetching items");

    var correlationId = UUID.randomUUID().toString();

    ItemsRequest request = new ItemsRequest();
    ItemResponse response =
            rabbitTemplate.convertSendAndReceiveAsType(
                    request,
                    buildMessagePostProcessor(correlationId),
                    new ParameterizedTypeReference<ItemResponse>() {});

    return response;
  }

  private static MessagePostProcessor buildMessagePostProcessor(String correlationId) {
    return message -> {
      message.getMessageProperties().setCorrelationId(correlationId);
      message.getMessageProperties().setHeader("X-Correlation-Id", correlationId);
      message.getMessageProperties().setHeader(AmqpHeaders.CORRELATION_ID, correlationId);
      return message;
    };
  }
}
