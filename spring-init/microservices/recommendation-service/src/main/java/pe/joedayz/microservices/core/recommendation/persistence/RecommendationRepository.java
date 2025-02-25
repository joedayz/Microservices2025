package pe.joedayz.microservices.core.recommendation.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * @author josediaz
 **/
public interface RecommendationRepository extends
    ReactiveCrudRepository<RecommendationEntity, String> {
  Flux<RecommendationEntity> findByProductId(int productId);
}
