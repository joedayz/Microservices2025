package pe.joedayz.microservices.composite.product;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@TestConfiguration
public class TestBuildPropertiesConfig {
    
    @Bean
    public BuildProperties buildProperties() {
        Properties properties = new Properties();
        properties.setProperty("version", "1.0.0-SNAPSHOT");
        properties.setProperty("name", "product-composite-service");
        properties.setProperty("group", "pe.joedayz.microservices.composite.product");
        return new BuildProperties(properties);
    }
} 