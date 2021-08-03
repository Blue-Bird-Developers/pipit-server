package com.bluebird.pipit.portal.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@ApiModel(value = "숙명포털 인증 요청")
@Value
public class PortalAuthRequest {
	@ApiModelProperty(value = "숙명포털 아이디", required = true)
	String portalId;

	@ApiModelProperty(value = "숙명포털 비밀번호", required = true)
	String portalPassword;
}
