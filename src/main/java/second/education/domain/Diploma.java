package second.education.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Diploma extends AbstractData<Integer> {

    private Integer id;
    private String pinfl;
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
    private Integer diplomaSerialId;
    private String diplomaSerialAndNumber;
    private String countryName;

    @ManyToOne(fetch = FetchType.LAZY)
    private EnrolleeInfo enrolleeInfo;
}
