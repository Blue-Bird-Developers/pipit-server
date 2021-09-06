package com.bluebird.pipit.support.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

// 기본 exception. custom exception 으로 확장하여 사용 가능
public class AttributeSupportException extends RuntimeException {
	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error.";

	private final Map<String, Object> attributes = new HashMap<>();

	public AttributeSupportException() {
		super(INTERNAL_SERVER_ERROR_MESSAGE);
	}

	public AttributeSupportException(String message) {
		super(message);
	}

	public AttributeSupportException(Throwable cause) {
		super(cause);
	}

	public AttributeSupportException(String message, Throwable cause) {
		super(message, cause);
	}

	public AttributeSupportException with(String key, @Nullable Object value) {
		this.attributes.put(key, value);
		return this;
	}

	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	public ExceptionCode getCode() {
		return GeneralExceptionCode.GENERAL_ERROR;
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();
		if (message == null) {
			message = "";
		}

		String attributes;
		if (this.attributes.isEmpty()) {
			attributes = "{}";
		} else {
			attributes = this.attributes.toString();
		}

		return message + " : " + attributes;
	}
}
