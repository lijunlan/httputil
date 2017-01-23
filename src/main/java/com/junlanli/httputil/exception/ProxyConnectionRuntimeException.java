package com.junlanli.httputil.exception;

/**
 * Copyright (C) 2015 - 2017 JUNLAN LI All Rights Reserved.
 *
 * @author lijunlan888@gmail.com
 *  2017-01-17
 */
public class ProxyConnectionRuntimeException extends RuntimeException {

    public ProxyConnectionRuntimeException() {
    }

    public ProxyConnectionRuntimeException(String message) {
        super(message);
    }

    public ProxyConnectionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProxyConnectionRuntimeException(Throwable cause) {
        super(cause);
    }
}
