package pe.joedayz.microservices.core.product.services;

import static java.util.logging.Level.FINE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import pe.joedayz.api.core.product.Product;
import pe.joedayz.api.core.product.ProductService;
import pe.joedayz.api.exceptions.InvalidInputException;
import pe.joedayz.api.exceptions.NotFoundException;
import pe.joedayz.microservices.core.product.persistence.ProductEntity;
import pe.joedayz.microservices.core.product.persistence.ProductRepository;
import pe.joedayz.util.http.ServiceUtil;

import java.time.Duration;
import java.util.Random;

@RestController
public class ProductServiceImpl implements ProductService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

  private final ServiceUtil serviceUtil;

  private final ProductRepository repository;

  private final ProductMapper mapper;

  @Autowired
  public ProductServiceImpl(ProductRepository repository, ProductMapper mapper, ServiceUtil serviceUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Mono<Product> createProduct(Product body) {

    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }

    ProductEntity entity = mapper.apiToEntity(body);
    Mono<Product> newEntity = repository.save(entity)
        .log(LOG.getName(), FINE)
        .onErrorMap(
            DuplicateKeyException.class,
            ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
        .map(e -> mapper.entityToApi(e));

    return newEntity;
  }

  @Override
  public Mono<Product> getProduct(int productId, int delay, int faultPercent) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    LOG.info("Will get product info for id={}", productId);

    return repository.findByProductId(productId)
            .map(e -> throwErrorIfBadLuck(e, faultPercent))
            .delayElement(Duration.ofSeconds(delay))
        .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
        .log(LOG.getName(), FINE)
        .map(e -> mapper.entityToApi(e))
        .map(e -> setServiceAddress(e));
  }


  private ProductEntity throwErrorIfBadLuck(ProductEntity entity, int faultPercent) {
    if(faultPercent == 0) {
      return entity;
    }
    int randomThreshold = getRandomNumber(1, 100);

    if(faultPercent < randomThreshold) {
      LOG.debug("We got lucky, no error ocurred, {} < {} ", faultPercent, randomThreshold);
    }else{
      LOG.info("Bad luck, an error ocurred, {} >= {} ", faultPercent, randomThreshold);
      throw  new RuntimeException("Something went wrong...");
    }
    return entity;
  }

  private final Random randomNumberGenerator = new Random();

  private int getRandomNumber(int min, int max) {
    if(max<min){
      throw new InvalidInputException("Max must be greater than min");
    }
    return randomNumberGenerator.nextInt((max - min) + 1) + min;
  }


  @Override
  public Mono<Void> deleteProduct(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }

    LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
    return repository.findByProductId(productId).log(LOG.getName(), FINE).map(e -> repository.delete(e)).flatMap(e -> e);
  }

  private Product setServiceAddress(Product e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }
}
