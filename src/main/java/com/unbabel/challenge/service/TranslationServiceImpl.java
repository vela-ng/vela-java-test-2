package com.unbabel.challenge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbabel.challenge.dto.ClientRequest;
import com.unbabel.challenge.dto.ClientResponse;
import com.unbabel.challenge.dto.UnBabelRequest;
import com.unbabel.challenge.dto.UnBabelResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TranslationServiceImpl implements TranslationService {
    @Autowired
    RestTemplate restTemplate;

    @Value("${unbabel.apikey}")
    private String apikey;

    @Value("${unbabel.username}")
    private String username;

    @Value("${unbabel.url}")
    private String unbabelUrl;

    private Logger log = LoggerFactory.getLogger(TranslationServiceImpl.class);

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpEntity<String> buildHttpEntity(Map<String,Object> headerParams, String requestBodyObj) {

        HttpHeaders headers = new HttpHeaders();
        headerParams.forEach((k,v)->headers.set(k, v.toString()));
        return new HttpEntity<String>(requestBodyObj, headers);
    }
    private HttpEntity<String> buildHttpEntity(Map<String,Object> headerParams) {

        HttpHeaders headers = new HttpHeaders();
        headerParams.forEach((k,v)->headers.set(k, v.toString()));
        return new HttpEntity<String>(headers);
    }

    private ClientResponse convertToClientResponse(UnBabelResponse apiResponse) {
        ClientResponse clientResponse = new ClientResponse();

        clientResponse.setFromLanguage(apiResponse.getSourceLanguage());
        clientResponse.setToLanguage(apiResponse.getTargetLanguage());
        clientResponse.setOriginalText(apiResponse.getText());
        clientResponse.setStatus(apiResponse.getStatus());
        clientResponse.setTranslatedText(apiResponse.getTranslatedText());

        return clientResponse;
    }

    private Map<String,Object> getHeader(){
        Map<String,Object> headersMap = Collections.unmodifiableMap(
                Stream.of(
                        new AbstractMap.SimpleEntry<>("Authorization", "ApiKey"),
                        new AbstractMap.SimpleEntry<>(username, apikey),
                        new AbstractMap.SimpleEntry<>("Accept", "application/json")
                ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

        return headersMap;
    }

    private ClientResponse getMockClientResponse(UnBabelRequest unBabelRequest){

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setOriginalText(unBabelRequest.getText());
        clientResponse.setTranslatedText(RandomStringUtils.randomAlphabetic(
                unBabelRequest.getText().length()
        ));
        clientResponse.setStatus("Completed");
        clientResponse.setFromLanguage(unBabelRequest.getSourceLanguage());
        clientResponse.setToLanguage(unBabelRequest.getTargetLanguage());

        return clientResponse;
    }

    @Override
    public ClientResponse translate(UnBabelRequest clientRequest) {

        UnBabelResponse ApiResponse = new UnBabelResponse();
        ResponseEntity<UnBabelResponse> response = null;

        Map<String,Object> headersMap = getHeader();

        HttpEntity<?> httpEntity = buildHttpEntity(headersMap, asJsonString(clientRequest));

        try{
            response = restTemplate.exchange( unbabelUrl ,
                    HttpMethod.POST, httpEntity, UnBabelResponse.class);

            ApiResponse = response.getBody();
        } catch (Exception exception){
            log.warn(exception.getMessage());
            return getMockClientResponse(clientRequest);
        }

        if (null == ApiResponse){
            // Todo: Add custom Exception
            throw new RuntimeException();
        }

        return convertToClientResponse(ApiResponse);
    }

    @Override
    public ClientResponse translate(String uId) {
        UnBabelResponse ApiResponse = new UnBabelResponse();
        ResponseEntity<UnBabelResponse> response = null;

        HttpEntity<?> httpEntity = buildHttpEntity(getHeader());

        try{
            response = restTemplate.exchange( unbabelUrl + "/{uId}" ,
                    HttpMethod.GET, httpEntity, UnBabelResponse.class, uId);

            ApiResponse = response.getBody();
        } catch (HttpStatusCodeException exception){
            throw exception;
        }

        if (null == ApiResponse){
            // Todo: Add custom Exception
            throw new RuntimeException();
        }

        return convertToClientResponse(ApiResponse);
    }

    @Override
    public UnBabelRequest convertToUnBabelRequest(ClientRequest clientRequest) {
        UnBabelRequest unBabelRequest = new UnBabelRequest();
        unBabelRequest.setSourceLanguage("en");
        unBabelRequest.setTargetLanguage(clientRequest.getTranslateTo());
        unBabelRequest.setText(clientRequest.getTextToTranslate());
        unBabelRequest.setTextFormat("text");

        return unBabelRequest;
    }
}
