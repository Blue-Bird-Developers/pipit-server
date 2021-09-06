package com.bluebird.pipit.support.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AuthenticationExceptionHandler extends AbstractGlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Problem> handleResourceNotFoundException(
		ResourceNotFoundException exception, NativeWebRequest request) {

		/* TODO : log message 통일
		log.warn();
		 */
		ProblemBuilder builder = this.createNotFoundProblemBuilder(exception);
		return create(exception, builder.build(), request);
	}

	protected ProblemBuilder createNotFoundProblemBuilder(Exception exception) {
		ProblemBuilder builder = Problem.builder()
			.withTitle(Status.NOT_FOUND.getReasonPhrase())
			.withStatus(Status.NOT_FOUND)
			.withDetail(exception.getMessage());
		this.applyAttributeSupports(builder, exception);
		return builder;
	}
}
