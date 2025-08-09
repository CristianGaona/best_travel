package com.best.travel.best_travel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
//@PropertySource("classpath:configs/api_currency.properties")
@PropertySources({
    @PropertySource("classpath:configs/api_currency.properties"),
    @PropertySource("classpath:configs/redis.properties")
})
public class PropertiesConfig {
    
}
