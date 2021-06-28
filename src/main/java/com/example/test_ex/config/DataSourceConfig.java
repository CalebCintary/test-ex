package com.example.test_ex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


import javax.sql.DataSource;

@Configuration
@PropertySources({@PropertySource("classpath:datasource.properties"),
                  @PropertySource("classpath:users.properties")})
public class DataSourceConfig {

    @Value("${datasource.driver}")
    private String driverClassName;

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.username}")
    private String username;

    @Value("${datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.driverClassName(driverClassName);
        builder.url(url);
        builder.username(username);
        builder.password(password);

        return builder.build();
    }
}
