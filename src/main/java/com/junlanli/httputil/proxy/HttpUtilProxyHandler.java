package com.junlanli.httputil.proxy;


import com.junlanli.httputil.delegation.HttpUtilDelegation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @author lijunlan888@gmail.com
 *  2017-01-13
 */
public class HttpUtilProxyHandler implements InvocationHandler {


    private HttpUtilDelegation delegation;

    public HttpUtilProxyHandler(HttpUtilDelegation delegation) {
        this.delegation = delegation;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        ProxyConnection proxyConnection = new ProxyConnection(o, method, objects);
        proxyConnection.parse();
        return proxyConnection.proxy(delegation);
    }

}
