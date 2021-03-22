package com.bluebird.pipit.support.exception;

public class InvalidBusinessSupportException extends AttributeSupportException {
	private final ExceptionCode code;

	public InvalidBusinessSupportException(String message, ExceptionCode code) {
		super(message);
		this.code = code;
	}

	public InvalidBusinessSupportException(String message) {
		this(message, GeneralExceptionCode.INVALID_BUSINESS);
	}

	@Override
	public ExceptionCode getCode() {
		return this.code;
	}
}
