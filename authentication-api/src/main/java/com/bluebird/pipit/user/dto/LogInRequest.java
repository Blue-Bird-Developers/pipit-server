package com.bluebird.pipit.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@ApiModel(description = "로그인 요청")
@Value
public class LogInRequest {
	@ApiModelProperty(value = "피핏 아이디", required = true)
	String pipitId;

	@ApiModelProperty(value = "피핏 비밀번호", required = true)
	String pipitPassword;

	int deviceId;
	int appVersion;
	int OSVersion;
	String deviceName;
	String tokenKey;
}
