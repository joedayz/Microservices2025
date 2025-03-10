package pe.joedayz.microservices.util.http;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;

/**
 * @author josediaz
 **/
public class HttpErrorInfo {
  private final ZonedDateTime timestamp;
  private final String path;
  private final HttpStatus httpStatus;
  private final String message;

  public HttpErrorInfo(){
    timestamp = null;
    path = null;
    httpStatus = null;
    message = null;
  }

  public HttpErrorInfo( HttpStatus httpStatus, String path,
      String message) {
    this.timestamp = ZonedDateTime.now();
    this.path = path;
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public String getPath() {
    return path;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public String getMessage() {
    return message;
  }
}
