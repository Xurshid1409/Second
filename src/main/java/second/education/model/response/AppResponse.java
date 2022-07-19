package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.Application;
import second.education.domain.EnrolleeInfo;
import second.education.domain.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse {

    private EnrolleeResponse enrolleeResponse;
    private DiplomaResponse diplomaResponse;
    private String phoneNumber;
    private ApplicationResponse applicationResponse;

    Integer Id;

    private Integer tilId;

    private String tilName;

    Integer ShaklId;

    String shaklName;

    Integer directionId;

    String directionName;

    Integer futureInstitutionId;

    String futureInstitutionName;

    String appStatus;

    Boolean diplomaStatus;

    String createdDate;

    public AppResponse(Application application){
        tilId=application.getLanguage().getId();
        tilName=application.getLanguage().getLanguage();
        directionId=application.getEduForm().getDirection().getId();
        directionName=application.getEduForm().getDirection().getName();
        futureInstitutionId=application.getFutureInstitution().getId();
        futureInstitutionName=application.getFutureInstitution().getName();
        appStatus=application.getStatus();
        diplomaStatus=application.getDiplomaStatus();
    }
}
