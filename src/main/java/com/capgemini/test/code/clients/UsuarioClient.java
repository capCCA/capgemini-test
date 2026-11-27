package com.capgemini.test.code.clients;

import org.springframework.boot.web.client.RestTemplateBuilder;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UsuarioClient {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(3))  // establecer conexión
            .setReadTimeout(Duration.ofSeconds(5))     // esperar respuesta
            .build();
    }
}