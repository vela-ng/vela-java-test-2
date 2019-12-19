package com.unbabel.challenge.service;

import com.unbabel.challenge.dto.ClientRequest;
import com.unbabel.challenge.dto.ClientResponse;
import com.unbabel.challenge.dto.UnBabelRequest;
import org.springframework.stereotype.Service;

@Service
public interface TranslationService {
    ClientResponse translate(UnBabelRequest clientRequest);
    ClientResponse translate(String uId);
    UnBabelRequest convertToUnBabelRequest(ClientRequest clientRequest);
}
