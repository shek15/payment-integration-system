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
/**
 * Executes outbound HTTP requests to external provider APIs.
 */
public class HttpServiceEngine {

    private final RestClient restClient;

    /**
     * Calls the configured external endpoint and returns the raw response body.
     *
     * @param httpRequest outbound request metadata and payload
     * @return raw HTTP response from the external provider
     */
    @SuppressWarnings("null")
    public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
        log.info("Calling external service at {}", httpRequest.getUrl());

        ResponseEntity<String> responseEntity = restClient.method(HttpMethod.POST)
            .uri(httpRequest.getUrl())
            .headers(restClientpHeaders -> restClientpHeaders.addAll(httpRequest.getHttpHeaders()))
            .body(httpRequest.getBody())
            .retrieve()
            .toEntity(String.class);

        log.info("Received external response with status code {}", responseEntity.getStatusCode());
        log.debug("External response body length: {}",
            responseEntity.getBody() != null ? responseEntity.getBody().length() : 0);

        return responseEntity;
    }
}
