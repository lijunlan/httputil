package com.junlanli.httputil.annotation;


import com.junlanli.httputil.enums.BodyType;

import java.lang.annotation.*;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @Author: lijunlan888@gmail.com
 * @Date: 2017-01-15
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BodyContent {

    BodyType value() default BodyType.STRING;

}
