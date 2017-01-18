package com.junlanli.httputil.factory;

import com.junlanli.httputil.binding.HttpUtilRegister;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @Author: lijunlan888@gmail.com
 * @Date: 2017-01-15
 */
public class HttpUtilFactoryBean<T> implements InitializingBean, FactoryBean<T> {

    private static final Logger logger = Logger.getLogger(HttpUtilFactoryBean.class);

    private Class<T> httpUtilInterface;

    private HttpUtilRegister httpUtilRegister;

    public HttpUtilFactoryBean() {

    }

    public HttpUtilFactoryBean(Class<T> httpUtilInterface) {
        this.httpUtilInterface = httpUtilInterface;
    }


    public Class<T> getHttpUtilInterface() {
        return httpUtilInterface;
    }

    public void setHttpUtilInterface(Class<T> httpUtilInterface) {
        this.httpUtilInterface = httpUtilInterface;
    }

    public HttpUtilRegister getHttpUtilRegister() {
        return httpUtilRegister;
    }

    public void setHttpUtilRegister(HttpUtilRegister httpUtilRegister) {
        this.httpUtilRegister = httpUtilRegister;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.httpUtilInterface, "Property \'httpUtilInterface\' is required");
        Assert.notNull(this.httpUtilRegister, "Property \'httpUtilDelegation\' is required");
    }

    @Override
    public T getObject() throws Exception {
        return httpUtilRegister.getHttpUtil(httpUtilInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return httpUtilInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
