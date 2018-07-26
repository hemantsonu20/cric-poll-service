package com.github.hemantsonu20.cric.exception;

import org.springframework.http.HttpStatus;

public class WebServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    /*
     * Defaults
     * 
     */

    public WebServiceException() {
        super();
    }

    public WebServiceException(String msg) {

        super(msg);
    }

    public WebServiceException(String msg, Throwable t) {

        super(msg, t);
    }

    /*
     * Customs
     * 
     */
    public WebServiceException(HttpStatus httpStatus) {

        this.httpStatus = httpStatus;
    }

    public WebServiceException(HttpStatus httpStatus, String msg) {

        super(msg);
        this.httpStatus = httpStatus;
    }

    public WebServiceException(HttpStatus httpStatus, String msg, Throwable t) {

        super(msg, t);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {

        return httpStatus;
    }

    @Override
    public String toString() {

        return String.join(" ", super.toString(), httpStatus.toString());
    }
}