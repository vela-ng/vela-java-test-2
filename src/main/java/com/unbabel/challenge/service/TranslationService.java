package com.unbabel.challenge.service;

import com.unbabel.challenge.dto.ClientRequest;
import com.unbabel.challenge.dto.ClientResponse;
import org.springframework.stereotype.Service;

@Service
public interface TranslationService {
    public ClientResponse translate(ClientRequest clientRequest);
    public ClientResponse translate(String uId);
}
