package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.classificator.Direction;
import second.education.domain.classificator.EduForm;
import second.education.domain.classificator.Language;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application extends AbstractData<Integer> {

    private String institutionName;
    private String speciality;
    private String educationType;
    private String status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Language language;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EduForm eduForm;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EnrolleeInfo enrolleeInfo;
}
