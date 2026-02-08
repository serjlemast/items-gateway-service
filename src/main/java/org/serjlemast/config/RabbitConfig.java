package org.serjlemast.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ITEMS_QUEUE = "items.rpc.queue";
    public static final String ITEMS_EXCHANGE = "items.exchange";
    public static final String ITEMS_ROUTING_KEY = "items.rpc";

    @Bean
    public Queue itemsQueue() {
        return new Queue(ITEMS_QUEUE);
    }

    @Bean
    public DirectExchange itemsExchange() {
        return new DirectExchange(ITEMS_EXCHANGE);
    }

    @Bean
    public Binding itemsBinding(Queue itemsQueue, DirectExchange itemsExchange) {
        return BindingBuilder.bind(itemsQueue).to(itemsExchange).with(ITEMS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonJsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonJsonMessageConverter);
        template.setReplyTimeout(5000);
        template.setExchange(ITEMS_EXCHANGE);
        template.setRoutingKey(ITEMS_ROUTING_KEY);
        template.setReplyAddress("async_remote_call_queue");
        template.afterPropertiesSet();
        return template;
    }

}
