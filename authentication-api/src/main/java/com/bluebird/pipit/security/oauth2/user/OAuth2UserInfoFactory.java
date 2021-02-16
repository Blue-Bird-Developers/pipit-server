package com.bluebird.pipit.security.oauth2.user;

import com.dsc.rankmakers.domain.user.domain.AuthProvider;
import com.dsc.rankmakers.global.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(registrationId + "is not supported yet..");
        }
    }
}

