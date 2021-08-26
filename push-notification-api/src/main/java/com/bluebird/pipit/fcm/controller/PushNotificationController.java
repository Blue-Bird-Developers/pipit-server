package com.bluebird.pipit.fcm.controller;

import com.bluebird.pipit.fcm.dto.PushNotificationRequest;
import com.bluebird.pipit.fcm.service.PushNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PushNotificationController {
	private final PushNotificationService pushNotificationService;

	public void sendMessage(@RequestBody PushNotificationRequest request) {
		pushNotificationService.sendMessageTo(request.getTargetToken(), request.getTitle(), request.getBody());
	}
}
