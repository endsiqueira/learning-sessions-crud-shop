package com.exs.learningsessionscrudshop.health;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.sql.Connection;

@Component
public class AppHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(10)) {
                return Health.up().withDetail("database", "Conexão com o banco de dados está OK").build();
            } else {
                return Health.down().withDetail("database", "Conexão com o banco de dados falhou").build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
