package com.solution.reddy.domain.user.info.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.solution.reddy.domain.user.entity.SocialType;
import com.solution.reddy.domain.user.info.OAuth2UserInfo;
import lombok.Getter;

@Getter
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    private String id;
    private String name;
    private String email;
    private String imageUrl;

    private final SocialType socialType = SocialType.GOOGLE;

    public GoogleOAuth2UserInfo(GoogleIdToken.Payload payload) {
        this.id = payload.getSubject();
        this.email = payload.getEmail();
        this.name = (String) payload.get("name");
        this.imageUrl = (String) payload.get("picture");
    }

    public String getSocialTypeName() {
        return socialType.toString();
    }
}
