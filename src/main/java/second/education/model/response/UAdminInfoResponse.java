package second.education.model.response;

import lombok.Getter;
import lombok.Setter;
import second.education.domain.AdminEntity;
import second.education.domain.classificator.University;

@Getter
@Setter
public class UAdminInfoResponse {

    private Integer id;
    private String firstname;
    private String lastname;
    private Integer futureInstitutionId;
    private String futureInstitutionName;


    public UAdminInfoResponse(AdminEntity adminEntity) {
        this.id = adminEntity.getId();
        this.firstname = adminEntity.getFistName();
        this.lastname = adminEntity.getLastname();
        if (adminEntity.getFutureInstitution() != null) {
            this.futureInstitutionId = adminEntity.getFutureInstitution().getId();
            this.futureInstitutionName = adminEntity.getFutureInstitution().getName();

        } else {
            University university = adminEntity.getUniversities().stream().findFirst().get();
            this.futureInstitutionId = university.getInstitutionId();
            this.futureInstitutionName = university.getInstitutionName();
        }
    }
}
