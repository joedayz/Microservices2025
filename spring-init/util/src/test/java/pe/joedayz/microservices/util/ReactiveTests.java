package pe.joedayz.microservices.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * @author josediaz
 **/
public class ReactiveTests {

  @Test
  public void testFlux(){
    List<Integer> numbers = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .filter(number -> number % 2 == 0)
        .map(number -> number * 2)
        .log()
        .collectList().block();

    assertThat(numbers).containsExactly(4, 8, 12, 16, 20);
  }
}
