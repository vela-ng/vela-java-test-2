package com.unbabel.challenge.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Data
@Service
@ConfigurationProperties(prefix = "unbabel")
public class ApiKey {
    private String username;
    private String url;
    private String apiKey;
}