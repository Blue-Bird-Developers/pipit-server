package com.bluebird.pipit.fcm.service;

import com.bluebird.pipit.fcm.domain.PushNotificationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {
	private static final String FIREBASE_CONFIG_PATH = "firebase/firebase_service_key.json";	 // TODO : add firebase project service key file
	private static final String API_URL = "https://fcm.googleapis.com/v1/projects/{firebase-project-id}/messages:send";
	private static final String OAUTH_SCOPE = "https://www.googleapis.com/auth/cloud-platform";

	private final ObjectMapper objectMapper;


	private String getAccessToken() {
		GoogleCredentials googleCredentials;
		try {
			googleCredentials = GoogleCredentials
				.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())
				.createScoped(List.of(OAUTH_SCOPE));
		} catch (IOException e) {
			throw new RuntimeException("Failed to open Firebase Configuration File.", e);
		}

		try {
			googleCredentials.refreshIfExpired();
		} catch (IOException e) {
			log.error("Failed to refresh Google Credentials.", e);
		}

		return googleCredentials.getAccessToken().getTokenValue();
	}

	private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
		PushNotificationMessage message = PushNotificationMessage.builder()
			.message(
				PushNotificationMessage.Message.builder()
					.token(targetToken)
					.notification(
						PushNotificationMessage.Notification.builder()
							.title(title)
							.body(body)
							.image(null)
							.build()
					).build())
			.validate_only(false)
			.build();

		return objectMapper.writeValueAsString(message);

	}

	public void sendMessageTo(String targetToken, String title, String body) {
		String message;
		try {
			message = makeMessage(targetToken, title, body);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to make Message.", e);
		}

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

		Request request = new Request.Builder()
			.url(API_URL)
			.post(requestBody)
			.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
			.build();

		try {
			client.newCall(request).execute();
		} catch (IOException e) {
			log.error("Failed to send request.", e);
		}
	}



}
