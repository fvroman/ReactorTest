package example;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class AppConfig extends AbstractR2dbcConfiguration{
        @Override
        @Bean
        public ConnectionFactory connectionFactory() {
            return ConnectionFactories.get("r2dbc:postgresql://test:test@dbpostgresql:5432/mydb");
        }
    }
