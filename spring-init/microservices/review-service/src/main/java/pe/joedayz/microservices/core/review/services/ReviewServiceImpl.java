package pe.joedayz.microservices.core.review.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import pe.joedayz.microservices.api.core.review.Review;
import pe.joedayz.microservices.api.core.review.ReviewService;
import pe.joedayz.microservices.api.exceptions.InvalidInputException;
import pe.joedayz.microservices.core.review.persistence.ReviewEntity;
import pe.joedayz.microservices.core.review.persistence.ReviewRepository;
import pe.joedayz.microservices.util.http.ServiceUtil;

/**
 * @author josediaz
 **/
@RestController
public class ReviewServiceImpl implements ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);
  private final ReviewMapper mapper;
  private final ReviewRepository repository;
  private final ServiceUtil serviceUtil;

  public ReviewServiceImpl(ReviewMapper mapper, ReviewRepository reviewRepository, ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
    this.repository = reviewRepository;
    this.mapper = mapper;
  }

  @Override
  public Review createReview(Review body) {
    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId());
    }
  }

  @Override
  public List<Review> getReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> apiList = mapper.entityListToApiList(entityList);
    apiList.forEach(r -> r.setServiceAddress(serviceUtil.getServiceAddress()));

    LOG.debug("getReviews: response size: {}", apiList.size());

    return apiList;
  }

  @Override
  public void deleteReviews(int productId) {
    repository.deleteAll(repository.findByProductId(productId));
  }
}
