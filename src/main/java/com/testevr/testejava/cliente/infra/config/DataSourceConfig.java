package com.testevr.testejava.cliente.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * DataSource configuration using pure JDBC (no ORM or Spring Data JDBC)
 * Uses DriverManagerDataSource for direct JDBC access
 */
@Configuration
public class DataSourceConfig {
    @Value("${postgres.driver}")
    private String driver;

    @Value("${postgres.ip}")
    private String ip;

    @Value("${postgres.port}")
    private String port;

    @Value("${postgres.database}")
    private String database;

    @Value("${postgres.username}")
    private String username;

    @Value("${postgres.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl("jdbc:postgresql://" + ip + ":" + port + "/" + database);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
