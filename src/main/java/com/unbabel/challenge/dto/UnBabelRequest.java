package com.unbabel.challenge.dto;

import lombok.Data;

@Data
public class UnBabelRequest {
    private String text;
    private String targetLanguage;
    private String sourceLanguage;
    private String textFormat;
}
