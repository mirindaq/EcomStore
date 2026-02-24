package iuh.fit.ecommerce.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "product.exchange";
    public static final String QUEUE_INDEX = "product.index.queue";
    public static final String ROUTING_KEY = "product.created";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue indexQueue() {
        return new Queue(QUEUE_INDEX);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(indexQueue())
                .to(exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
