package com.paranormal.controller;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.paranormal.dto.response.ErrorModel;
import com.paranormal.exception.ErrorCode;
import com.paranormal.exception.ParanormalException;



@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler{

	
	Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);
	
	@ExceptionHandler(ParanormalException.class)
	public ResponseEntity<ErrorModel> exc(ParanormalException ex, WebRequest request){
		logger.info("Paranormal Exception, {}", ex.toString());
		ErrorModel errorModel = ErrorModel.builder()
				.timestamp(ZonedDateTime.now())
				.errorCode(ex.getErrorCode())
				.message(ex.getMessage())
				.statusCode(ex.getHttpCode())
				.build();
		return new ResponseEntity<>(errorModel, HttpStatus.resolve(ex.getHttpCode()));
	}
}
