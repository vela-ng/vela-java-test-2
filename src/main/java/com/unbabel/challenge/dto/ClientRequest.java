package com.unbabel.challenge.dto;

import lombok.Data;

@Data
public class ClientRequest {
    private String textToTranslate;
    private String translateTo;
}
