package second.education.model.request;

import lombok.Getter;
import lombok.Setter;

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


}
