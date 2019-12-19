package com.unbabel.challenge.dto;

import lombok.Data;

@Data
public class UnbabelRequest {
    private String source;
    private String targetLanguage;
    private String sourceLanguage;
    private String textFormat;
}
