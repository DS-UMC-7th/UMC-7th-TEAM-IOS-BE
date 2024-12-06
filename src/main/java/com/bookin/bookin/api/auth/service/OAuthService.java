package com.bookin.bookin.api.auth.service;

import com.bookin.bookin.domain.user.entity.User;
import com.bookin.bookin.domain.user.entity.enums.Provider;
import com.bookin.bookin.domain.user.repository.UserRepository;
import com.bookin.bookin.security.JwtTokenProvider;
import com.bookin.bookin.api.auth.dto.LoginResponseDTO;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.client_secret}")
    private String clientSecret;

    public String getAuthorizationUrl() {
        StringBuilder url = new StringBuilder();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=").append(clientId);
        url.append("&redirect_uri=").append(redirectUri);
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    public ResponseEntity<ApiResponse<LoginResponseDTO>> processKakaoCallback(String code) {
        String accessToken = fetchAccessToken(code);
        String userInfo = fetchUserInfo(accessToken);

        User user = processUser(userInfo);
        String token = jwtTokenProvider.createToken(user.getUserId());

        LoginResponseDTO loginResponse = new LoginResponseDTO(user.getUserId(), token);
        ApiResponse<LoginResponseDTO> response = ApiResponse.onSuccess(loginResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private String fetchAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String tokenParams = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&code=" + code +
                "&client_secret=" + clientSecret;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> tokenResponse = restTemplate.exchange(
                tokenUrl + "?" + tokenParams,
                HttpMethod.POST,
                null,
                String.class
        );

        return parseAccessToken(tokenResponse.getBody());
    }

    private String fetchUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        return userInfoResponse.getBody();
    }

    private String parseAccessToken(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private User processUser(String userInfo) {
        String userId = parseUserId(userInfo);
        String email = parseEmail(userInfo);
        String nickname = parseNickname(userInfo);

        User user = userRepository.findByUserId(userId)
                .orElseGet(() -> User.builder()
                        .userId(userId)
                        .password("")
                        .nickname(nickname)
                        .provider(Provider.KAKAO)
                        .providerId(Long.parseLong(userId))
                        .email(email)
                        .build());

        return userRepository.save(user);
    }

    private String parseUserId(String userInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userInfo);
            return jsonNode.get("id").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseEmail(String userInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userInfo);
            JsonNode kakaoAccount = jsonNode.get("kakao_account");
            return kakaoAccount.get("email").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseNickname(String userInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userInfo);
            JsonNode properties = jsonNode.get("properties");
            return properties.get("nickname").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
