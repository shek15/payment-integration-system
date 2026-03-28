package com.mycompany.payments.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpServiceEngine {
    private final RestClient restClient;

    public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
        log.info("Making Http call to external service...");

        @SuppressWarnings("null")
        ResponseEntity<String> responseEntity =  restClient.method(httpRequest.getHttpMethod() != null ? httpRequest.getHttpMethod() : HttpMethod.POST)
        .uri(httpRequest.getUrl())
        .headers(restClientpHeaders -> restClientpHeaders.addAll(httpRequest.getHttpHeaders()))
        .body(httpRequest.getBody())
        .retrieve()
        .toEntity(String.class);

        log.info("\nHttp call response from HttpServiceEngine: {}", responseEntity);

        return responseEntity;
    }

}
