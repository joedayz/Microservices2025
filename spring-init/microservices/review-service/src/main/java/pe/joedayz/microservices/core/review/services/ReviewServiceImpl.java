package pe.joedayz.microservices.core.review.services;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final ReviewRepository reviewRepository;
  private final ServiceUtil serviceUtil;

  public ReviewServiceImpl(ReviewMapper mapper, ReviewRepository reviewRepository, ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
    this.reviewRepository = reviewRepository;
    this.mapper = mapper;
  }

  @Override
  public List<Review> getReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    List<ReviewEntity> entityList = reviewRepository.findByProductId(productId);
    List<Review> apiList = mapper.entityListToApiList(entityList);
    apiList.forEach(r -> r.setServiceAddress(serviceUtil.getServiceAddress()));

    LOG.debug("getReviews: response size: {}", apiList.size());

    return apiList;
  }
}
