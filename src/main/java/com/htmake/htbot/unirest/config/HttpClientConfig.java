package com.htmake.htbot.unirest.config;

import com.htmake.htbot.unirest.HttpClient;
import com.htmake.htbot.unirest.impl.HttpClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClient() {
        return new HttpClientImpl();
    }
}
