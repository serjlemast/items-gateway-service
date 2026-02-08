package org.serjlemast.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Value("${rabbit.request.queue}")
  private String requestQueueName;

  @Value("${rabbit.request.exchange}")
  private String requestExchange;

  @Value("${rabbit.request.routing-key}")
  private String requestRoutingKey;

  @Value("${rabbit.reply.queue}")
  private String replyQueueName;

  @Bean
  public Queue itemsQueue() {
    return new Queue(requestQueueName);
  }

  @Bean
  public DirectExchange itemsExchange() {
    return new DirectExchange(requestExchange);
  }

  @Bean
  public Binding itemsBinding(Queue itemsQueue, DirectExchange itemsExchange) {
    return BindingBuilder.bind(itemsQueue).to(itemsExchange).with(requestRoutingKey);
  }

  @Bean
  public MessageConverter jacksonJsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, MessageConverter jacksonJsonMessageConverter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jacksonJsonMessageConverter);
    template.setReplyTimeout(5000);
    template.setExchange(requestExchange);
    template.setRoutingKey(requestRoutingKey);
    template.setReplyAddress(replyQueueName);
    template.afterPropertiesSet();
    return template;
  }
}
