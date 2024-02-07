package com.exs.learningsessionscrudshop.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitingService {

    private final RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public RateLimitingService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public boolean allowRequisition(String key, long capacity, long windowInSeconds) {
        Long currentTokens = valueOperations.increment(key, -1);
        if (currentTokens == null || currentTokens < 0) {
            // Se a chave não existir ou os tokens forem esgotados, inicialize a chave com a capacidade menos um (já consumindo um token)
            valueOperations.set(key, String.valueOf(capacity - 1), windowInSeconds, TimeUnit.SECONDS);
            return true; // Permite a requisição pois é a primeira dentro da janela de tempo
        } else {
            // Se a chave existir e os tokens não forem esgotados, atualiza o tempo de expiração
            redisTemplate.expire(key, windowInSeconds, TimeUnit.SECONDS);
            return currentTokens > 0;
        }
    }
}
