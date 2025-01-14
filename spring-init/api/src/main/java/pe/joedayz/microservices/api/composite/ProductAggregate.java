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

  public ProductAggregate(ServiceAddresses serviceAddresses, List<ReviewSummary> reviews,
      List<RecommendationSummary> recommendations, int weight, String name, int productId) {
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
