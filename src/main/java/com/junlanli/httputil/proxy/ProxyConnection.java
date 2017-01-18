package com.junlanli.httputil.proxy;

import com.alibaba.fastjson.JSON;
import com.junlanli.httputil.annotation.*;
import com.junlanli.httputil.builder.HeaderBuilder;
import com.junlanli.httputil.delegation.HttpUtilDelegation;
import com.junlanli.httputil.enums.BodyType;
import com.junlanli.httputil.enums.HttpMethod;
import com.junlanli.httputil.exception.ProxyConnectionRuntimeException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @Author: lijunlan888@gmail.com
 * @Date: 2017-01-17
 */
public class ProxyConnection {

    private static final Logger logger = Logger.getLogger(ProxyConnection.class);

    private String url;

    private HttpHeaders httpHeaders;

    private Class<?> responseType;

    private Class<?> responseGenericReturnType;

    private HttpMethod httpMethod;

    private HttpEntity httpEntity;

    private List<Param> urlVParams;

    private List<Param> urlVMParams;

    private List<Param> bodyCParams;

    private List<Param> hostParams;

    private class Param {

        private Parameter parameter;

        private Object value;

        public Param(Parameter parameter, Object value) {
            this.parameter = parameter;
            this.value = value;
        }

        public Parameter getParameter() {
            return parameter;
        }

        public void setParameter(Parameter parameter) {
            this.parameter = parameter;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    public ProxyConnection(Object object, Method method, Object[] objects) {
        init(object, method, objects);
    }

    public void parse() {
        try {
            //process annotation of UrlVariable
            urlVMParams.stream().filter(param -> param.getValue() instanceof Map).forEach(param -> {
                Map map = (Map) param.getValue();
                Iterator it = map.keySet().iterator();
                while (it.hasNext()) {
                    Object k = it.next();
                    Object v = map.get(k);
                    try {
                        url = url.replaceAll("\\{" + k.toString() + "\\}", URLEncoder.encode(v.toString(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        url = url.replaceAll("\\{" + k.toString() + "\\}", v.toString());
                    }
                }
            });

            //process annotation of UrlVariableMap
            urlVParams.forEach(param -> {
                try {
                    url = url.replaceFirst("\\{[^{,^}]*\\}", URLEncoder.encode(param.getValue().toString(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    url = url.replaceFirst("\\{[^{,^}]*\\}", param.getValue().toString());
                }
            });

            //process annotation of Host
            if (hostParams.size() > 0) {
                Param param = hostParams.get(0);
                url = param.getValue().toString() + url;
            }

            //process annotation of BodyContent
            if (bodyCParams.size() > 0) {
                //ignore the param which has the index larger than 0
                Param param = bodyCParams.get(0);
                BodyContent bodyContent = param.getParameter().getAnnotation(BodyContent.class);
                BodyType bodyType = bodyContent.value();
                switch (bodyType) {
                    case STRING:
                        if (httpHeaders.getContentType() == null) {
                            httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                        }
                        httpEntity = new HttpEntity(param.getValue().toString(), httpHeaders);
                        break;
                    case JSON:
                        if (!param.getParameter().getType().isAssignableFrom(JSON.class)) break;
                        if (httpHeaders.getContentType() == null) {
                            httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                        }
                        httpEntity = new HttpEntity(param.getValue(), httpHeaders);
                        break;
                    case FORM:
                        if (httpHeaders.getContentType() == null) {
                            httpHeaders.setContentType(MediaType.parseMediaType("application/x-www-form-urlencoded;charset=UTF-8"));
                        }
                        if (param.getValue() instanceof Map) {
                            StringBuilder sb = new StringBuilder();
                            Map map = (Map) param.getValue();
                            Iterator it = map.keySet().iterator();
                            while (it.hasNext()) {
                                Object k = it.next();
                                Object v = map.get(k);
                                String key = URLEncoder.encode(k.toString(), "utf-8");
                                sb.append(key);
                                sb.append("=");
                                String value = URLEncoder.encode(v.toString(), "utf-8");
                                sb.append(value);
                                sb.append("&");
                            }
                            if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);

                            httpEntity = new HttpEntity(sb.toString(), httpHeaders);
                        } else if (param.getParameter().getType().isAssignableFrom(String.class)) {
                            String content = (String) param.getValue();
                            httpEntity = new HttpEntity(content, httpHeaders);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            logger.error("proxy connection parse failed", e);
            throw new ProxyConnectionRuntimeException("proxy connection parse failed");
        }
    }

    public Object proxy(HttpUtilDelegation httpUtilDelegation) {
        try {
            if (httpEntity == null) {
                httpEntity = new HttpEntity(httpHeaders);
            }
            ResponseEntity responseEntity = null;
            Class<?> type = responseGenericReturnType == null ? responseType : responseGenericReturnType;
            URI uri = new URI(url);
            logger.info(uri);
            switch (httpMethod) {
                case GET:
                    responseEntity = httpUtilDelegation.get(uri, httpEntity, type);
                    break;
                case POST:
                    responseEntity = httpUtilDelegation.post(uri, httpEntity, type);
                    break;
                case DELETE:
                    responseEntity = httpUtilDelegation.delete(uri, httpEntity, type);
                    break;
                case PUT:
                    responseEntity = httpUtilDelegation.put(uri, httpEntity, type);
                    break;
                case OPTIONS:
                    responseEntity = httpUtilDelegation.options(uri, httpEntity, type);
                    break;
                case HEAD:
                    responseEntity = httpUtilDelegation.head(uri, httpEntity, type);
                    break;
                case PATCH:
                    responseEntity = httpUtilDelegation.patch(uri, httpEntity, type);
                    break;
                case TRACE:
                    responseEntity = httpUtilDelegation.trace(uri, httpEntity, type);
                    break;
                default:
                    break;
            }
            if (responseType.isAssignableFrom(ResponseEntity.class)) {
                return responseEntity;
            }
            if (responseEntity != null) {
                return responseEntity.getBody();
            }
            return null;
        } catch (Exception e) {
            logger.error("proxy connection parse failed", e);
            throw new ProxyConnectionRuntimeException("proxy connection parse failed");
        }
    }

    private void init(Object object, Method method, Object[] objects) {
        try {
            Api apiConfig = method.getAnnotation(Api.class);
            httpMethod = apiConfig.method();
            url = apiConfig.url();
            Class<? extends HeaderBuilder> clazz = apiConfig.headerBuilder();
            HeaderBuilder builder = clazz.newInstance();
            httpHeaders = builder.build();

            responseType = method.getReturnType();
            if (responseType.isAssignableFrom(ResponseEntity.class)) {
                Type type = method.getGenericReturnType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] args = parameterizedType.getActualTypeArguments();
                    if (args.length > 0) {
                        responseGenericReturnType = (Class<?>) args[0];
                    } else {
                        responseGenericReturnType = String.class;
                    }
                } else {
                    responseGenericReturnType = String.class;
                }
            }

            urlVMParams = new ArrayList<>();
            urlVParams = new ArrayList<>();
            bodyCParams = new ArrayList<>();
            hostParams = new ArrayList<>();

            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                UrlVariable urlVariable = parameter.getAnnotation(UrlVariable.class);
                if (urlVariable != null) {
                    urlVParams.add(new Param(parameter, objects[i]));
                    continue;
                }
                UrlVariableMap urlVariableMap = parameter.getAnnotation(UrlVariableMap.class);
                if (urlVariableMap != null) {
                    urlVMParams.add(new Param(parameter, objects[i]));
                    continue;
                }
                BodyContent bodyContent = parameter.getAnnotation(BodyContent.class);
                if (bodyContent != null) {
                    bodyCParams.add(new Param(parameter, objects[i]));
                    continue;
                }
                Host host = parameter.getAnnotation(Host.class);
                if (host != null) {
                    hostParams.add(new Param(parameter, objects[i]));
                    continue;
                }
                if (parameter.getType().isAssignableFrom(HttpHeaders.class)) {
                    httpHeaders = (HttpHeaders) objects[i];
                }
            }
        } catch (Exception e) {
            logger.error("proxy connection parse failed", e);
            throw new ProxyConnectionRuntimeException("proxy connection init failed");
        }
    }
}
