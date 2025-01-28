package pe.joedayz.microservices.core.review.services;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pe.joedayz.microservices.api.core.review.Review;
import pe.joedayz.microservices.core.review.persistence.ReviewEntity;

/**
 * @author josediaz
 **/
@Mapper(componentModel = "spring")
public interface ReviewMapper {

  @Mappings({
      @Mapping(target = "serviceAddress", ignore = true)
  })
  Review entityToApi(ReviewEntity entity);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "version", ignore = true),
  })
  ReviewEntity apiToEntity(Review api);

  List<Review> entityListToApiList(List<ReviewEntity> entityList);

  List<ReviewEntity> apiListToEntityList(List<Review> apiList);
}
