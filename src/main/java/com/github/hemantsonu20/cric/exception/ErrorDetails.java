package com.github.hemantsonu20.cric.exception;

import com.github.hemantsonu20.cric.model.BaseBean;

public class ErrorDetails extends BaseBean {

	private final int code;
	private final String message;
	private final Object details;

	public ErrorDetails(int code, String message, Object details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}

	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public Object getDetails() {
		return details;
	}
}
