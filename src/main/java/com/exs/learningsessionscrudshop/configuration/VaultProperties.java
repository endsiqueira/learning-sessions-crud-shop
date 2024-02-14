package com.exs.learningsessionscrudshop.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.vault")
public class VaultProperties {

    private String host;
    private int port;
    private String token;

}
