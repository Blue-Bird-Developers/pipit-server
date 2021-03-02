package com.bluebird.pipit.support.exception;

import lombok.Value;

// TODO : Swagger 연동
@Value
public class ProblemResponseModel {
	// 에러 제목
	String title;

	// 에러 상태 코드
	int status;

	// 에러 메시지
	String detail;

	// 에러 구분 코드
	ExceptionCode code;
}
