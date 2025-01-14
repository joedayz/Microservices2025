package pe.joedayz.microservices.core.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import pe.joedayz.microservices.api.core.product.Product;
import pe.joedayz.microservices.api.core.product.ProductService;
import pe.joedayz.microservices.api.exceptions.InvalidInputException;
import pe.joedayz.microservices.api.exceptions.NotFoundException;
import pe.joedayz.microservices.util.http.ServiceUtil;

/**
 * @author josediaz
 **/
@RestController
public class ProductServiceimpl implements ProductService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductServiceimpl.class);

  private final ServiceUtil serviceUtil;

  public ProductServiceimpl(ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Product getProduct(int productId) {
    LOG.debug("Return the found product with id {}", productId);

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    if (productId == 13) {
      throw new NotFoundException("No product found for productId: " + productId);
    }

    return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
  }
}
