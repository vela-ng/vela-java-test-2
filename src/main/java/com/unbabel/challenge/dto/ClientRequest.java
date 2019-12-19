package com.unbabel.challenge.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
public class ClientRequest {
    @NotBlank
    @Max(500)
    private String textToTranslate;

    @Max(3)
    @NonNull
    private String translateTo;
}
