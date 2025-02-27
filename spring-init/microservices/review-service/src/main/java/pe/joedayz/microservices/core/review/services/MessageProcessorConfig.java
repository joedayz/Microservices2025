package pe.joedayz.microservices.core.review.services;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.joedayz.microservices.api.event.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.joedayz.microservices.api.core.review.Review;
import pe.joedayz.microservices.api.core.review.ReviewService;
import pe.joedayz.microservices.api.exceptions.EventProcessingException;

/**
 * @author josediaz
 **/
@Configuration
public class MessageProcessorConfig {
  private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

  private final ReviewService reviewService;

  public MessageProcessorConfig(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @Bean
  public Consumer<Event<Integer, Review>> messageProcessor() {
    return event -> {
      LOG.info("Process message created at {}...", event.getEventCreatedAt());
      switch (event.getEventType()) {
        case CREATE:
          Review review = event.getData();
          LOG.info("Create review with ID: {}/{}", review.getProductId(), review.getReviewId());
          reviewService.createReview(review).block();
          break;
        case DELETE:
          int productId = event.getKey();
          LOG.info("Delete reviews with ProductID: {}", productId);
          reviewService.deleteReviews(productId).block();
          break;
        default:
          String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
          LOG.warn(errorMessage);
          throw new EventProcessingException(errorMessage);
      }
      LOG.info("Message processing done!");
    };
  }
}
