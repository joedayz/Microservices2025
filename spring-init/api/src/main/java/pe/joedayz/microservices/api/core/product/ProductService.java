package pe.joedayz.microservices.api.core.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author josediaz
 **/
public interface ProductService {

  @GetMapping(value = "/product/{productId}", produces = "application/json")
  Product getProduct(@PathVariable int productId);
}
