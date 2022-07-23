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

    Integer id;

    private Integer tilId;

    private String tilName;

    Integer shaklId;

    String shaklName;

    Integer directionId;

    String directionName;

    Integer futureInstitutionId;

    String futureInstitutionName;

    String appStatus;

    String diplomaStatus;

    String createdDate;

    public AppResponse(Application application) {
        this.id = application.getId();
        this.tilId = application.getLanguage().getId();
        this.shaklId = application.getEduForm().getId();
        this.shaklName = application.getEduForm().getName();
        this.tilName = application.getLanguage().getLanguage();
        this.directionId = application.getEduForm().getDirection().getId();
        this.directionName = application.getEduForm().getDirection().getName();
        this.futureInstitutionId = application.getFutureInstitution().getId();
        this.futureInstitutionName = application.getFutureInstitution().getName();
        this.appStatus = application.getStatus();
        this.diplomaStatus = String.valueOf(application.getDiplomaStatus());
        this.createdDate = application.getCreatedDate().toString();
    }
}
