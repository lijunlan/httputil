package com.junlanli.httputil.factory;


import com.junlanli.httputil.delegation.HttpUtilDelegation;
import com.junlanli.httputil.proxy.HttpUtilProxyHandler;

import java.lang.reflect.Proxy;


/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @author lijunlan888@gmail.com
 *  2017-01-15
 */
public class HttpUtilProxyFactory<T> {

    private final Class<T> httpUtilInterface;

    public HttpUtilProxyFactory(Class<T> mapperInterface) {
        this.httpUtilInterface = mapperInterface;
    }

    protected T newInstance(HttpUtilProxyHandler httpUtilProxyHandler) {
        return (T) Proxy.newProxyInstance(httpUtilInterface.getClassLoader(), new Class[]{httpUtilInterface}, httpUtilProxyHandler);
    }

    public T newInstance(HttpUtilDelegation httpUtilDelegation) {
        HttpUtilProxyHandler httpUtilProxyHandler = new HttpUtilProxyHandler(httpUtilDelegation);
        return this.newInstance(httpUtilProxyHandler);
    }

}
