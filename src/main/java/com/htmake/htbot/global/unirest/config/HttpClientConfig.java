package com.htmake.htbot.global.unirest.config;

import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClient() {
        return new HttpClientImpl();
    }
}
