package second.education.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import second.education.api_model.ApiConstant;
import second.education.api_model.one_id.OneIdResponseToken;
import second.education.api_model.one_id.OneIdResponseUserInfo;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class OneIdServiceApi {

    private final String client_id = "vakansiya.edu.uz";
    private final String scope = "second.edu.uz";
    private final String redirect_uri = "http://localhost:8080/api/public/oneId/signIn/qabul.jsp";
    private final String client_secret = "EN8CdZBJtqWpMfhK7ASj6rUb";
    private final WebClient webClient;

    public URI redirectOneIdUrl() {
        String state = "testState";
        String response_type = "one_code";
        String oneIdUrl = "https://sso2.egov.uz:8443/sso/oauth/Authorization.do?" +
                "state=" + state +
                "&redirect_uri=" + redirect_uri +
                "&response_type=" + response_type +
                "&client_id=" + client_id +
                "&scope="+ scope;
        return URI.create(oneIdUrl);
    }

    public OneIdResponseToken getAccessAndRefreshToken(String code) {

        String grant_type = "one_authorization_code";
        return webClient.post()
                .uri("https://sso2.egov.uz:8443/sso/oauth/Authorization.do?" +
                        "grant_type=" + grant_type +
                        "&client_id=" + client_id +
                        "&client_secret=" + client_secret +
                        "&code=" + code +
                        "&redirect_uri=" + redirect_uri)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(ApiConstant.ONE_ID_LOGIN, ApiConstant.ONE_ID_PASSWORD))
                .retrieve()
                .bodyToMono(OneIdResponseToken.class)
                .block();
    }

    public OneIdResponseUserInfo getUserInfo(String accessToken) {
        String grant_type = "one_access_token_identify";
        return webClient.post()
                .uri("https://sso2.egov.uz:8443/sso/oauth/Authorization.do?" +
                        "grant_type="+ grant_type +
                        "&client_id=" + client_id +
                        "&client_secret=" + client_secret +
                        "&access_token=" + accessToken +
                        "&scope=" + scope)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(ApiConstant.ONE_ID_LOGIN, ApiConstant.ONE_ID_PASSWORD))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OneIdResponseUserInfo.class)
                .block();
    }
}
