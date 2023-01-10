package com.service.apigateway;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private static final RestTemplate rest;
    static  {
        rest = new RestTemplate();
    }
    public static ResponseEntity request(String url, HttpMethod method, HttpHeaders headers, String body){
        var entity = new HttpEntity<>(body, headers);
        var response = rest.exchange(url,method,entity,String.class);
        return response;
    }
}
