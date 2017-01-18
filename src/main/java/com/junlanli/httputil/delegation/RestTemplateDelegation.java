package com.junlanli.httputil.delegation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @Author: lijunlan888@gmail.com
 * @Date: 2017-01-15
 */
public class RestTemplateDelegation implements HttpUtilDelegation {

    private RestTemplate restTemplate;

    public RestTemplateDelegation(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity get(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }

    @Override
    public ResponseEntity post(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    @Override
    public ResponseEntity put(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
    }

    @Override
    public ResponseEntity delete(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, responseType);
    }

    @Override
    public ResponseEntity options(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.OPTIONS, entity, responseType);
    }

    @Override
    public ResponseEntity head(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.HEAD, entity, responseType);
    }

    @Override
    public ResponseEntity patch(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType);
    }

    @Override
    public ResponseEntity trace(URI url, HttpEntity entity, Class<?> responseType) {
        return restTemplate.exchange(url, HttpMethod.TRACE, entity, responseType);
    }
}
