package com.bluebird.pipit.support.exception;

public class ServiceInvalidStatusException extends AttributeSupportException {
	private final ExceptionCode code;

	public ServiceInvalidStatusException(String message, ExceptionCode code) {
		super(message);
		this.code = code;
	}

	public ServiceInvalidStatusException(String message) {
		this(message, GeneralExceptionCode.SERVICE_INVALID);
	}

	@Override
	public ExceptionCode getCode() {
		return this.code;
	}
}
