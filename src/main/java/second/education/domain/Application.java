package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application extends AbstractData<Integer> {

    private String institutionName;
    private String speciality;
    private String educationType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Language language;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    private EnrolleeInfo enrolleeInfo;
}
