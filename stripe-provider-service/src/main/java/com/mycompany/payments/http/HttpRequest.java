package com.mycompany.payments.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;

import lombok.Data;

@Data
public class HttpRequest {
    private HttpMethod httpMethod;
    private String url;
    private HttpHeaders httpHeaders;
    private Object body;
}
