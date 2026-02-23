package iuh.fit.ecommerce.messaging.rabbitmq.publisher;

import iuh.fit.ecommerce.configurations.RabbitConfig;
import iuh.fit.ecommerce.events.ProductCreatedEvent;
import iuh.fit.ecommerce.messaging.rabbitmq.message.ProductCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void publishProduct(ProductCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                new ProductCreatedMessage(event.productId())
        );
    }
}