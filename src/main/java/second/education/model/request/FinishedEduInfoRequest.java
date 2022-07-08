package second.education.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinishedEduInfoRequest {

    private Integer institutionCountryId;
    private String institutionName;
    private String institutionOldName;
    private String specialityName;
    private String specialityOldName;
    private String eduFormName;
    private String diplomaSerialAndNumber;
    private String eduFinishingDate;
}
