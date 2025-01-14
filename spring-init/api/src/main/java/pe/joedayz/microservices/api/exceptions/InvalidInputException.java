package pe.joedayz.microservices.api.exceptions;

/**
 * @author josediaz
 **/
public class InvalidInputException extends RuntimeException {

  public InvalidInputException(String message) {
    super(message);
  }
  public InvalidInputException(String message, Throwable cause) {
    super(message, cause);
  }
  public InvalidInputException(Throwable cause) {
    super(cause);
  }
}
