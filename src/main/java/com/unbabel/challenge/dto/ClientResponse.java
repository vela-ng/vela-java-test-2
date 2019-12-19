package com.unbabel.challenge.dto;

import lombok.Data;

@Data
public class ClientResponse {
    private String fromLanguage;
    private String originalText;
    private String toLanguage;
    private String translatedText;
    private String status;
}
