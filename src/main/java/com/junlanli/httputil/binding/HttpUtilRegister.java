package com.junlanli.httputil.binding;


import com.junlanli.httputil.delegation.HttpUtilDelegation;
import com.junlanli.httputil.exception.BindingException;
import com.junlanli.httputil.factory.HttpUtilProxyFactory;

import java.io.File;
import java.util.*;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @Author: lijunlan888@gmail.com
 * @Date: 2017-01-15
 */
public class HttpUtilRegister {

    private final Map<Class<?>, HttpUtilProxyFactory<?>> knownUtils = new HashMap();

    private final HttpUtilDelegation httpUtilDelegation;

    public HttpUtilRegister(HttpUtilDelegation httpUtilDelegation) {
        this.httpUtilDelegation = httpUtilDelegation;
    }

    public <T> T getHttpUtil(Class<T> type) {
        HttpUtilProxyFactory httpUtilProxyFactory = (HttpUtilProxyFactory) knownUtils.get(type);
        if (httpUtilProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the HttpUtilRegister.");
        } else {
            try {
                return (T) httpUtilProxyFactory.newInstance(httpUtilDelegation);
            } catch (Exception e) {
                throw new BindingException("Error getting httpUtil instance. Cause: " + e, e);
            }
        }
    }

    public <T> boolean hasHttpUtil(Class<T> type) {
        return knownUtils.containsKey(type);
    }

    public <T> void addHttpUtil(Class<T> type) {
        if (type.isInterface()) {
            if (this.hasHttpUtil(type)) {
                throw new BindingException("Type " + type + " is already known to the HttpUtilRegister.");
            }

            boolean loadCompleted = false;

            try {
                knownUtils.put(type, new HttpUtilProxyFactory(type));
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    this.knownUtils.remove(type);
                }

            }
        }

    }

    public Collection<Class<?>> getHttpUtils() {
        return Collections.unmodifiableCollection(this.knownUtils.keySet());
    }

    public void addHttpUtils(String packageName) throws ClassNotFoundException {
        List<Class> classes = getClazz(packageName);
        classes.forEach(this::addHttpUtil);
    }

    private List<Class> getClazz(String filePath) throws ClassNotFoundException {
        List<Class> myClass = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles != null) {
            for (File childFile : childFiles) {
                if (childFile.isDirectory()) {
                    myClass.addAll(getClazz(childFile.getPath()));
                } else {
                    String childFilePath = childFile.getPath();
                    childFilePath = childFilePath.substring(childFilePath.indexOf("/classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("/", ".");
                    myClass.add(Class.forName(childFilePath));
                }
            }
        }
        return myClass;
    }

}
