package com.exs.learningsessionscrudshop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

@Configuration
public class VaultConfig {

    @Bean
    public VaultTemplate vaultTemplate(VaultProperties vaultProperties) {
        return new VaultTemplate(VaultEndpoint.create(vaultProperties.getHost(), vaultProperties.getPort()),
                new TokenAuthentication(vaultProperties.getToken()));
    }
}
