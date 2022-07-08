package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinishedEduInfo extends AbstractData<Integer> {

    private Integer institutionCountryId;
    private String institutionName;
    private String institutionOldName;
    private String specialityName;
    private String specialityOldName;
    private String eduFormName;
    private String diplomaSerialAndNumber;
    private String eduFinishingDate;
    //Active diploma
    private boolean actualDiploma = false;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EnrolleeInfo enrolleeInfo;

}
