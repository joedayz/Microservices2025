package pe.joedayz.microservices.core.review.services;

import static java.util.logging.Level.FINE;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import pe.joedayz.microservices.api.core.review.Review;
import pe.joedayz.microservices.api.core.review.ReviewService;
import pe.joedayz.microservices.api.exceptions.InvalidInputException;
import pe.joedayz.microservices.core.review.persistence.ReviewEntity;
import pe.joedayz.microservices.core.review.persistence.ReviewRepository;
import pe.joedayz.microservices.util.http.ServiceUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

/**
 * @author josediaz
 **/
@RestController
public class ReviewServiceImpl implements ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);
  private final ReviewMapper mapper;
  private final ReviewRepository repository;
  private final ServiceUtil serviceUtil;
  private final Scheduler jdbcScheduler;

  public ReviewServiceImpl(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, ReviewMapper mapper,
      ReviewRepository reviewRepository, ServiceUtil serviceUtil) {
    this.jdbcScheduler = jdbcScheduler;
    this.serviceUtil = serviceUtil;
    this.repository = reviewRepository;
    this.mapper = mapper;
  }

  @Override
  public Mono<Review> createReview(Review body) {
    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    return Mono.fromCallable(() -> internalCreateReview(body))
        .subscribeOn(jdbcScheduler);
  }

  public Review internalCreateReview(Review body) {
    try {
      ReviewEntity entity = mapper.apiToEntity(body);
      ReviewEntity newEntity = repository.save(entity);

      LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(),
          body.getReviewId());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException(
          "Duplicate key, Product Id: " + body.getProductId() + ", Review Id:"
              + body.getReviewId());
    }
  }

  @Override
  public Flux<Review> getReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    LOG.info("Will get reviews for product with id={}", productId);

    return Mono.fromCallable(() -> internalGetReviews(productId))
        .flatMapMany(Flux::fromIterable)
        .log(LOG.getName(), FINE)
        .subscribeOn(jdbcScheduler);
  }

  public List<Review> internalGetReviews(int productId) {
    List<ReviewEntity> entityList = repository.findByProductId(productId);
    List<Review> apiList = mapper.entityListToApiList(entityList);
    apiList.forEach(r -> r.setServiceAddress(serviceUtil.getServiceAddress()));

    LOG.debug("getReviews: response size: {}", apiList.size());

    return apiList;
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    return Mono.fromRunnable(() -> internalDeleteReviews(productId))
        .subscribeOn(jdbcScheduler).then();
  }

  public void internalDeleteReviews(int productId) {
    LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
    repository.deleteAll(repository.findByProductId(productId));
  }
}
