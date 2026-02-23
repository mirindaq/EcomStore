package iuh.fit.ecommerce.repositories.elasticsearch;

import iuh.fit.ecommerce.entities.elasticsearch.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {

}

