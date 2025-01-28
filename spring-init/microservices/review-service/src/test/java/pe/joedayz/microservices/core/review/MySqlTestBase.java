package pe.joedayz.microservices.core.review;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

/**
 * @author josediaz
 **/
public abstract class MySqlTestBase {

  @ServiceConnection
  static final JdbcDatabaseContainer database = new MySQLContainer("mysql:8.0.32").withStartupTimeoutSeconds(300);

  static
  {
    database.start();
  }
}
