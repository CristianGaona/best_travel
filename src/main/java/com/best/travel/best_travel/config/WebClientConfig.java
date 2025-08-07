package com.best.travel.best_travel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${api.base.url}")
    private String baseURL;
    @Value("${api.api-key}")
    private String apiKey;
    @Value("${api.api-key.header}")
    private String apiKeyHeader;
    
    @Bean(name="currency")
    public WebClient currencyWebClient() {
        return WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }

    @Bean(name="base")
    public WebClient baseWebClient() {
        return WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }
    
}
