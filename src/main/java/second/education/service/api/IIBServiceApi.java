package second.education.service.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import second.education.api_model.ApiConstant;
import second.education.api_model.iib_api.IIBResponse;
import second.education.model.request.IIBRequest;

@Service
public class IIBServiceApi {

    private final WebClient webClient;

    public IIBServiceApi(WebClient webClient) {
        this.webClient = webClient;
    }

    public IIBResponse iibResponse(IIBRequest iibRequest) {
        return webClient.post()
                .uri("http://172.18.9.169:9449/api/person-info-with-photo/")
                .headers(httpHeaders -> httpHeaders.setBasicAuth(ApiConstant.IIB_API_LOGIN, ApiConstant.IIB_API_PASSWORD))
                .bodyValue(iibRequest)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(IIBResponse.class)
                .block();
    }
}
