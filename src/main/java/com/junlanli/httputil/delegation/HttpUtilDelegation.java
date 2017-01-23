package com.junlanli.httputil.delegation;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @author lijunlan888@gmail.com
 *  2017-01-13
 */
public interface HttpUtilDelegation {

    ResponseEntity get(URI url, HttpEntity entity, Class<?> responseType);

    ResponseEntity post(URI url, HttpEntity entity, Class<?> responseType);

    ResponseEntity put(URI url, HttpEntity entity, Class<?> responseType);

    ResponseEntity delete(URI url, HttpEntity entity, Class<?> responseType);

    ResponseEntity options(URI url, HttpEntity entity, Class<?> responseType);

    ResponseEntity head(URI url, HttpEntity entity, Class<?> responseType);

    ResponseEntity patch(URI url, HttpEntity entity, Class<?> responseType);

    ResponseEntity trace(URI url, HttpEntity entity, Class<?> responseType);

}
