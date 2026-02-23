package iuh.fit.ecommerce.messaging.rabbitmq.consumer;

import iuh.fit.ecommerce.entities.Product;
import iuh.fit.ecommerce.events.ProductCreatedEvent;
import iuh.fit.ecommerce.repositories.ProductRepository;
import iuh.fit.ecommerce.services.ProductSearchService;
import iuh.fit.ecommerce.services.VectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductIndexingConsumer {

    private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;
    private final VectorStoreService vectorStoreService;

    @RabbitListener(queues = "product.created.queue")
    public void handleProductCreated(ProductCreatedEvent event) {

        Product product = productRepository
                .findForIndexing(event.productId())
                .orElseThrow();

        productSearchService.indexProduct(product);

        product.getProductVariants()
                .forEach(vectorStoreService::indexProductVariant);
    }
}
