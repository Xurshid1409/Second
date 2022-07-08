package second.education.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DiplomaRequest {

    private String countryName;
    private Integer institutionId;
    private String institutionName;
    private String eduFormName;
    private String eduFinishingDate;
    private String speciality;
    private String diplomaNumberAndSerial;
    private MultipartFile diploma;
    private MultipartFile diplomaIlova;
}
