package org.serjlemast.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenerContainerConfiguration {

  @Bean
  public Queue replyQueue(@Value("${rabbit.reply.queue}") String queueName) {
    return new Queue(queueName);
  }

  /*
   * Required for replay queue
   *
   * error:  RabbitTemplate is not configured as MessageListener - cannot use a 'replyAddress': blocking_rpc_reply_queue
   */
  @Bean
  public SimpleMessageListenerContainer replyListenerContainer(
      ConnectionFactory connectionFactory, RabbitTemplate rabbitTemplate, Queue replyQueue) {
    var container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueues(replyQueue);
    container.setMessageListener(rabbitTemplate);
    return container;
  }
}
