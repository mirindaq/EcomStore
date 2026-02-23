package iuh.fit.ecommerce.configurations;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
