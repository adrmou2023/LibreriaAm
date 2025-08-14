package com.alura.literatura.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CorsConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}