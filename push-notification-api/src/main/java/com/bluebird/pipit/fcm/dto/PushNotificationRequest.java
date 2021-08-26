package com.bluebird.pipit.fcm.dto;

import lombok.Value;

@Value
public class PushNotificationRequest {
	String targetToken;
	String title;
	String body;
}
