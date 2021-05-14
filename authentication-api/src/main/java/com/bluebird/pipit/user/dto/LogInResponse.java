package com.bluebird.pipit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogInResponse<T> {
	private int status;
	private String message;
	private String accessToken;
	private T data;
}
