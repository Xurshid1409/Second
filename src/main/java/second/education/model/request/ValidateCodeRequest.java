package second.education.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateCodeRequest {

    private String phoneNumber;
    private String code;
}
