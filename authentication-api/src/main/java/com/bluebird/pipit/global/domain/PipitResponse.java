package com.bluebird.pipit.global.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PipitResponse<T> {
	private int status;
	private String message;
	private T data;
}
