package com.mycompany.payments.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.mycompany.payments.constant.ErrorCodeEnum;
import com.mycompany.payments.exception.StripeProviderException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Executes outbound HTTP requests to external provider APIs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
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
        log.info("Calling external service at {} with method {}", httpRequest.getUrl(), httpRequest.getHttpMethod());

        try {
            ResponseEntity<String> responseEntity = restClient.method(httpRequest.getHttpMethod())
                                                    .uri(httpRequest.getUrl())
                                                    .headers(restClientpHeaders -> restClientpHeaders.addAll(httpRequest.getHttpHeaders()))
                                                    .body(httpRequest.getBody())
                                                    .retrieve()
                                                    .toEntity(String.class);

            log.info("Received external response with status code {}", responseEntity.getStatusCode().value());
            log.debug("External response body length: {}",
                responseEntity.getBody() != null ? responseEntity.getBody().length() : 0);

            return responseEntity;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.warn("External service returned error status {}", e.getStatusCode().value());

            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE ||
                e.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
                throw new StripeProviderException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorCode(),
                    ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorMessage());
            }

            return ResponseEntity.status(e.getStatusCode())
                                .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unexpected error while calling external service", e);
            throw new StripeProviderException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorCode(),
                ErrorCodeEnum.ERROR_CONNECTING_TO_EXTERNAL_SERVICE.getErrorMessage());
        }
    }
}
