package pe.joedayz.microservices.core.recommendation.persistence;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
/**
 * @author josediaz
 **/
public interface RecommendationRepository extends CrudRepository<RecommendationEntity, String> {
  List<RecommendationEntity> findByProductId(int productId);
}
