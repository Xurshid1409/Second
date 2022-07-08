package second.education.service.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import second.education.api_model.ApiConstant;
import second.education.api_model.sms_api.SMSAPIRequest;

@Service
@RequiredArgsConstructor
public class SmsServiceApi {

    private final WebClient webClient;

    public Void sendData(SMSAPIRequest request) {

            return this.webClient.post()
                    .uri(ApiConstant.SMS_API_URL)
                    .headers(httpHeader -> httpHeader.setBasicAuth(ApiConstant.SMS_API_LOGIN, ApiConstant.SMS_API_PASSWORD))
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
    }
}
