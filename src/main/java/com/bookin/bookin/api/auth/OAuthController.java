package com.bookin.bookin.api.auth;

import com.bookin.bookin.domain.user.entity.User;
import com.bookin.bookin.domain.user.entity.enums.Provider;
import com.bookin.bookin.domain.user.repository.UserRepository;
import com.bookin.bookin.security.JwtTokenProvider;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.client_secret}")
    private String clientSecret;

    @GetMapping("/kakao")
    public String kakaoConnect() {
        StringBuilder url = new StringBuilder();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + clientId);
        url.append("&redirect_uri=" + redirectUri);
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<String>> kakaoCallback(@RequestParam String code) {
        // 카카오 토큰 요청
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

        // 액세스 토큰 추출 및 사용자 정보 가져오기
        String accessToken = parseAccessToken(tokenResponse.getBody());
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 사용자 정보 처리
        User user = processUser(userInfoResponse.getBody());
        String token = jwtTokenProvider.createToken(user.getUserId());

        ApiResponse<String> response = ApiResponse.onSuccess("로그인 성공, JWT 토큰: " + token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 토큰에서 액세스 토큰 추출
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
        // 사용자 정보 파싱
        String userId = parseUserId(userInfo);
        String email = parseEmail(userInfo);
        String nickname = parseNickname(userInfo);

        // DB에서 사용자 확인 후 신규 사용자 추가
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

    // 사용자 정보 파싱
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

    private String parseProfilePicture(String userInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userInfo);
            JsonNode properties = jsonNode.get("properties");
            return properties.get("profile_image").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
