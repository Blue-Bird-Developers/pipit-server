package com.bluebird.pipit.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@ApiModel(value = "회원가입 요청")
@Value
public class SignUpRequest {
	@ApiModelProperty(value = "숙명포털 아이디")
	String portalId;

	@ApiModelProperty(value = "숙명포털 비밀번호")
	String portalPassword;

	@ApiModelProperty(value = "숙명포털 인증 성공 여부", required = true)
	boolean portalSuccess;

	@ApiModelProperty(value = "피핏 아이디", required = true)
	String pipitId;

	@ApiModelProperty(value = "피핏 비밀번호", required = true)
	String pipitPassword;
}
