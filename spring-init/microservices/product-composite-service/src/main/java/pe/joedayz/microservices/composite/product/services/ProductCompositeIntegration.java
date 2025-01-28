package pe.joedayz.microservices.composite.product.services;

import static org.springframework.http.HttpMethod.GET;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pe.joedayz.microservices.api.core.product.Product;
import pe.joedayz.microservices.api.core.product.ProductService;
import pe.joedayz.microservices.api.core.recommendation.Recommendation;
import pe.joedayz.microservices.api.core.recommendation.RecommendationService;
import pe.joedayz.microservices.api.core.review.Review;
import pe.joedayz.microservices.api.core.review.ReviewService;
import pe.joedayz.microservices.api.exceptions.InvalidInputException;
import pe.joedayz.microservices.api.exceptions.NotFoundException;
import pe.joedayz.microservices.util.http.HttpErrorInfo;

/**
 * @author josediaz
 **/
@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService,
    ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;

  public ProductCompositeIntegration(RestTemplate restTemplate,
      ObjectMapper objectMapper,
      @Value("${app.product-service.host}") String productServiceHost,
      @Value("${app.product-service.port}") String productServicePort,
      @Value("${app.recommendation-service.host}") String recommendationServiceHost,
      @Value("${app.recommendation-service.port}") String recommendationServicePort,
      @Value("${app.review-service.host}") String reviewServiceHost,
      @Value("${app.review-service.port}") String reviewServicePort) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;

    this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
    this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
    this.reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
  }

  @Override
  public Product getProduct(int productId) {
    try {
      String url = productServiceUrl + productId;
      LOG.debug("Call getProduct API on URL: {}", url);

      Product product = restTemplate.getForObject(url, Product.class);
      LOG.debug("Found product with id : {}", product.getProductId());

      return product;
    }catch (HttpClientErrorException ex){
      switch (HttpStatus.resolve(ex.getStatusCode().value())){
        case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));
        case UNPROCESSABLE_ENTITY -> throw new InvalidInputException(getErrorMessage(ex));
        default -> throw  ex;
      }
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try{
      return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    }catch (IOException ioex){
      return ex.getMessage();
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    try{
      String url = recommendationServiceUrl + productId;
      LOG.debug("Call getRecommendations API on URL: {}", url);

      List<Recommendation> recommendations = restTemplate.exchange(url, GET, null,
          new ParameterizedTypeReference<List<Recommendation>>() {
          }).getBody();

      LOG.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);

      return recommendations;
    }catch (Exception ex){
      LOG.warn("Got an exception while getting recommendations: {}", ex.getMessage());
      return new ArrayList<>();
    }

  }

  @Override
  public List<Review> getReviews(int productId) {
    try{
      String url = reviewServiceUrl + productId;
      LOG.debug("Call getReviews API on URL: {}", url);

      List<Review> reviews = restTemplate.exchange(url, GET, null,
          new ParameterizedTypeReference<List<Review>>() {
          }).getBody();

      LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);

      return reviews;
    }catch (Exception ex){
      LOG.warn("Got an exception while getting reviews: {}", ex.getMessage());
      return new ArrayList<>();
    }
  }
}
