package com.junlanli.httputil.annotation;


import com.junlanli.httputil.enums.BodyType;

import java.lang.annotation.*;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @author lijunlan888@gmail.com
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BodyContent {

    BodyType value() default BodyType.STRING;

}
