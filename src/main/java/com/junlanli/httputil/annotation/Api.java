package com.junlanli.httputil.annotation;


import com.junlanli.httputil.builder.DefaultHeaderBuilder;
import com.junlanli.httputil.builder.HeaderBuilder;
import com.junlanli.httputil.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @Author: lijunlan888@gmail.com
 * @Date: 2017-01-13
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {

    String url();

    HttpMethod method() default HttpMethod.GET;

    Class<? extends HeaderBuilder> headerBuilder() default DefaultHeaderBuilder.class;

}
