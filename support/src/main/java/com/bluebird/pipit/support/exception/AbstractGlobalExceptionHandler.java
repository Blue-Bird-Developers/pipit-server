package com.bluebird.pipit.support.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGlobalExceptionHandler implements ProblemHandling {

	@ExceptionHandler
	public ResponseEntity<Problem> handleRuntimeException(
		RuntimeException exception, NativeWebRequest request
	) {
		AttributeSupportException attributeSupportException;
		if (exception instanceof AttributeSupportException) {
			attributeSupportException = (AttributeSupportException)exception;
		} else {
			attributeSupportException = new AttributeSupportException(exception);
		}

		// TODO : log.error
		return this.createInternalServerErrorResponse(attributeSupportException, request);
	}

	@ExceptionHandler
	public ResponseEntity<Problem> handleInvalidRequestParameterException(
		InvalidRequestParameterException exception, NativeWebRequest request
	) {
		// TODO : log.warn
		ProblemBuilder builder = this.createBadRequestResponse(exception);
		return create(exception, builder.build(), request);
	}

	@ExceptionHandler
	public ResponseEntity<Problem> handleInvalidBusinessSupportException(
		InvalidBusinessSupportException exception, NativeWebRequest request
	) {
		// TODO : log.error
		return this.createInternalServerErrorResponse(exception, request);
	}

	protected ResponseEntity<Problem> createInternalServerErrorResponse(
		Exception exception, NativeWebRequest request
	) {
		ProblemBuilder builder = Problem.builder()
			.withTitle(Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
			.withStatus(Status.INTERNAL_SERVER_ERROR)
			.withDetail(exception.getMessage());

		this.applyAttributeSupports(builder, exception);

		ThrowableProblem problem = builder.build();
		return create(exception, problem, request);
	}

	protected ProblemBuilder createBadRequestResponse(Exception exception) {
		ProblemBuilder builder = Problem.builder()
			.withTitle(Status.BAD_REQUEST.getReasonPhrase())
			.withStatus(Status.BAD_REQUEST)
			.withDetail(exception.getMessage());
		this.applyAttributeSupports(builder, exception);
		return builder;
	}

	protected void applyAttributeSupports(ProblemBuilder builder, Exception exception) {
		if (exception instanceof AttributeSupportException) {
			AttributeSupportException attributeException = (AttributeSupportException)exception;
			builder.with("code", attributeException.getCode());
			attributeException.getAttributes()
				.forEach((key, value) -> builder.with(key, value));
		}
	}
}
