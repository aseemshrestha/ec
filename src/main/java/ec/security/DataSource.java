package ec.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Base64;
import java.util.Map;

@Configuration
public class DataSource {
    @Autowired
    private Environment environment;

    @Bean
    public javax.sql.DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(environment.getProperty("spring.datasource.url"));
        dataSourceBuilder.username(environment.getProperty("spring.datasource.username"));
        dataSourceBuilder
                .password(new String(Base64.getDecoder()
                        .decode(environment.getProperty("spring.datasource.password"))));
        //  dataSourceBuilder.password(environment.getProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }
}
