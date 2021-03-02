package com.bluebird.pipit.support.exception;

public class RuleViolationAttributeSupportException extends AttributeSupportException {
	private final ExceptionCode code;

	public RuleViolationAttributeSupportException(String message) {
		super(message);
		this.code = GeneralExceptionCode.RULE_VIOLATION;
	}

	@Override
	public ExceptionCode getCode() {
		return this.code;
	}
}
