package com.unbabel.challenge.dto;

import lombok.Data;

@Data
public class UnBabelResponse {
    private String text;
    private String textFormat;
    private int price;
    private String sourceLanguage;
    private String targetLanguage;
    private String status;
    private String uId;
    private String client;
    private int balance;
    private String translatedText;

}
