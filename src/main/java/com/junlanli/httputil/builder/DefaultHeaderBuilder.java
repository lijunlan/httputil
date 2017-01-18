package com.junlanli.httputil.builder;

import org.springframework.http.HttpHeaders;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @Author: lijunlan888@gmail.com
 * @Date: 2017-01-15
 */
public class DefaultHeaderBuilder implements HeaderBuilder{

    @Override
    public HttpHeaders build() {
        HttpHeaders httpHeaders = new HttpHeaders();
        return httpHeaders;
    }
}
