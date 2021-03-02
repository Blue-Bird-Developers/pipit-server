package com.bluebird.pipit.support.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;

public class InvalidRequestParameterException extends AttributeSupportException {
	public static final String INVALID_REQUEST_PARAMETER_ERROR_MESSAGE = "Some parameters are invalid.";

	public InvalidRequestParameterException(BindException exception) {
		super(INVALID_REQUEST_PARAMETER_ERROR_MESSAGE, exception);
	}

	public InvalidRequestParameterException(ConstraintViolationException exception) {
		super(INVALID_REQUEST_PARAMETER_ERROR_MESSAGE, exception);
	}

	@Override
	public ExceptionCode getCode() {
		return GeneralExceptionCode.INVALID_PARAMETER;
	}
}
