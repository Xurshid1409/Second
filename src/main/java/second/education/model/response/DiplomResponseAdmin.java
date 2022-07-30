package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.Diploma;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiplomResponseAdmin {
    private Integer id;
    private String countryName;
    private Integer institutionId;
    private String institutionName;
    private Integer institutionOldNameId;
    private String institutionOldName;
    private Integer degreeId;
    private String degreeName;
    private Integer eduFormId;
    private String eduFormName;
    private Integer specialityId;
    private String specialityName;
    private String eduFinishingDate;
    private String diplomaStatus;
    private String diplomaNumberAndDiplomaSerial;

    private EnrolleeResponse enrolleeResponse;
    private FileResponse fileResponse;
    public DiplomResponseAdmin(Diploma diploma, FileResponse fileResponse) {
        this.id = diploma.getId();
        this.countryName = diploma.getCountryName();
        this.institutionId = diploma.getInstitutionId();
        this.institutionName = diploma.getInstitutionName();
        this.institutionOldNameId = diploma.getInstitutionOldNameId();
        this.institutionOldName = diploma.getInstitutionOldName();
        this.eduFormName = diploma.getEduFormName();
        this.degreeId = diploma.getDegreeId();
        this.degreeName = diploma.getDegreeName();
        this.specialityId = diploma.getSpecialityId();
        this.specialityName = diploma.getSpecialityName();
        this.eduFinishingDate = diploma.getEduFinishingDate();
        this.diplomaNumberAndDiplomaSerial = diploma.getDiplomaSerialAndNumber();
        this.fileResponse = fileResponse;
    }

    public DiplomResponseAdmin(Diploma diploma) {
        this.id = diploma.getId();
        this.countryName = diploma.getCountryName();
        this.institutionId = diploma.getInstitutionId();
        this.institutionName = diploma.getInstitutionName();
        this.institutionOldNameId = diploma.getInstitutionOldNameId();
        this.institutionOldName = diploma.getInstitutionOldName();
        this.eduFormName = diploma.getEduFormName();
        this.degreeId = diploma.getDegreeId();
        this.degreeName = diploma.getDegreeName();
        this.specialityId = diploma.getSpecialityId();
        this.specialityName = diploma.getSpecialityName();
        this.eduFinishingDate = diploma.getEduFinishingDate();
        this.diplomaNumberAndDiplomaSerial = diploma.getDiplomaSerialAndNumber();
    }
}
