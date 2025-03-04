package pe.joedayz.microservices.composite.product;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.joedayz.microservices.composite.product.services.ProductCompositeIntegration;

/**
 * @author josediaz
 **/
@Configuration
public class HealthCheckConfiguration {

  @Autowired
  ProductCompositeIntegration integration;

  @Bean
  ReactiveHealthContributor coreServices() {
    final Map<String, ReactiveHealthIndicator> registry = new LinkedHashMap<>();

    registry.put("product", ()-> integration.getProductHealth());
    registry.put("product", ()-> integration.getProductHealth());
    registry.put("product", ()-> integration.getProductHealth());

    return CompositeReactiveHealthContributor.fromMap(registry);
  }
}
