package pe.joedayz.microservices.api.composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author josediaz
 **/
public interface ProductCompositeService {

  @GetMapping
      (value = "/product-composite/{productId}",
          produces = "application/json")
  ProductAggregate getProduct(@PathVariable int productId);
}
