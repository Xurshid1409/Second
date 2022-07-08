package second.education.api_model.sms_api;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class SMSAPIRequest {

    private String phone_number;
    private Integer code = ThreadLocalRandom.current().nextInt(99999, 1000000);
    private String message = "Kasb.edu.uz tizimiga kirish uchun parol " + code;
}
