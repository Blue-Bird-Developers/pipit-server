package com.bluebird.pipit.user.dto;

import javax.annotation.Nonnull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@ApiModel(value = "피핏 계정 확인 요청", description = "비밀번호 변경에 앞서, 숙명포털 계정 및 피핏 아이디에 해당하는 피핏 계정이 있는지 확인하기 위한 요청")
@Value
public class UserCheckRequest {
	@ApiModelProperty(value = "피핏 아이디", required = true)
	@Nonnull
	String pipitId;

	@ApiModelProperty(value = "숙명포털 아이디", required = true)
	@Nonnull
	String portalId;

	@ApiModelProperty(value = "숙명포털 비밀번호", required = true)
	@Nonnull
	String portalPassword;
}
