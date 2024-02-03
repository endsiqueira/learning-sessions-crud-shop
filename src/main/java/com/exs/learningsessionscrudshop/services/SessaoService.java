package com.exs.learningsessionscrudshop.services;


import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SessaoService {

    private final RedisTemplate<String, String> redisTemplate;

    public String criarSessao(String usuario) {
        String sessaoId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("sessao:" + sessaoId, usuario, 30 * 60, TimeUnit.SECONDS);
        return sessaoId;
    }
}