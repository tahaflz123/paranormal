package com.paranormal.exception;

public enum ErrorCode {
	
	UNAUTHORIZED(401),
	CREADENTIALS_NOT_MATCHING(401),
	EMAIL_ALREADY_EXISTS(409),
	POST_HEADER_CONFLICT(409),
	ACCOUT_ALREADY_EXISTS(409),
    NO_RESOURCE(410),
	TOKEN_EXPIRED(410);
	
	private final int httpCode;
	
	ErrorCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public int getHttpCode() {
		return httpCode;
	}

}
