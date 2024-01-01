package clevertec.config;

import clevertec.cache.Cache;
import clevertec.cache.impl.LfuCache;
import clevertec.cache.impl.LruCache;
import clevertec.entity.Product;
import clevertec.mapper.ProductMapper;
import clevertec.mapper.ProductMapperImpl;
import clevertec.servlet.ProductServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.UUID;

@Configuration
@ComponentScan(basePackages = "clevertec")
public class AppConfig {

    @Value("${database.url}")
    private String dbUrl;

    @Value("${database.username}")
    private String dbUsername;

    @Value("${database.password}")
    private String dbPassword;

    @Value("${database.driver-class-name}")
    private String dbDriverClassName;

    @Value("${database.sql-path}")
    private String sqlPathKey;

    @Value("${database.initialize-db}")
    private Boolean initializeDatabase;

    @Value("${cache.type}")
    private String cacheType;

    @Value("${cache.capacity}")
    private int cacheCapacity;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        propertyConfigurer.setProperties(Objects.requireNonNull(yaml.getObject()));
        return propertyConfigurer;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setUsername(dbUsername);
        hikariConfig.setPassword(dbPassword);
        hikariConfig.setDriverClassName(dbDriverClassName);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ProductMapper productMapper() {
        return new ProductMapperImpl();
    }

    @Bean
    public Cache<UUID, Product> productCache() {
        return switch (cacheType) {
            case "lru" -> new LruCache<>(cacheCapacity);
            case "lfu" -> new LfuCache<>(cacheCapacity);
            default -> throw new IllegalArgumentException("Unsupported cache type: " + cacheType);
        };
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(sqlPathKey);
        liquibase.setShouldRun(initializeDatabase);
        return liquibase;
    }

    @Bean
    public ProductServlet productServlet() {
        return new ProductServlet();
    }
}
