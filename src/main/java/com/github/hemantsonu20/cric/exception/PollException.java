package com.github.hemantsonu20.cric.exception;

import org.springframework.http.HttpStatus;

public class PollException extends WebServiceException {

	private static final long serialVersionUID = 1L;

	public PollException(HttpStatus status) {
		super(status);
	}

	public PollException(HttpStatus status, String msg) {
		super(status, msg);
	}

	public PollException(HttpStatus status, String msg, Throwable cause) {
		super(status, msg, cause);
	}
}
