package com.paranormal.exception;

public class ParanormalException extends RuntimeException{

	private final ErrorCode errorCode;
	private final String errorMessage;
	
	public ParanormalException(ErrorCode errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public int getHttpCode() {
		return errorCode.getHttpCode();
	}
	
	public String getErrorCode() {
		return errorCode.name();
	}
	@Override
	public String getMessage() {
		return errorMessage;
	}
}