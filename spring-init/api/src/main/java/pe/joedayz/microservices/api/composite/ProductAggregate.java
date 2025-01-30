package pe.joedayz.microservices.api.composite;

import java.util.List;

/**
 * @author josediaz
 **/
public class ProductAggregate {
  private final int productId;
  private final String name;
  private final int weight;
  private final List<RecommendationSummary> recommendations;
  private final List<ReviewSummary> reviews;
  private final ServiceAddresses serviceAddresses;

  public ProductAggregate() {
    productId = 0;
    name = null;
    weight = 0;
    recommendations = null;
    reviews = null;
    serviceAddresses = null;
  }

  public ProductAggregate( int productId, String name, int weight,
      List<RecommendationSummary> recommendations, List<ReviewSummary> reviews,ServiceAddresses serviceAddresses) {
    this.serviceAddresses = serviceAddresses;
    this.reviews = reviews;
    this.recommendations = recommendations;
    this.weight = weight;
    this.name = name;
    this.productId = productId;
  }

  public int getProductId() {
    return productId;
  }

  public String getName() {
    return name;
  }

  public int getWeight() {
    return weight;
  }

  public List<RecommendationSummary> getRecommendations() {
    return recommendations;
  }

  public List<ReviewSummary> getReviews() {
    return reviews;
  }

  public ServiceAddresses getServiceAddresses() {
    return serviceAddresses;
  }
}
