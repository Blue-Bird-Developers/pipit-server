package com.bluebird.pipit.user.dto;

import javax.annotation.Nonnull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@ApiModel(value = "비밀번호 재설정 요청")
@Value
public class PasswordResetRequest {
	@ApiModelProperty(value = "숙명포털 인증 성공 여부", required = true)
	boolean authSuccess;

	@ApiModelProperty(value = "피핏 아이디", required = true)
	@Nonnull
	String pipitId;

	@ApiModelProperty(value = "피핏 비밀번호", required = true)
	@Nonnull
	String pipitPassword;

	@ApiModelProperty(value = "숙명포털 아이디")
	String portalId;

	@ApiModelProperty(value = "숙명포털 비밀번호")
	String portalPassword;
}
