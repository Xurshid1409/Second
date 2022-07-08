package second.education.domain.classificator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.AbstractData;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specialities extends AbstractData<Integer> {

    private Integer specialitiesId;
    private Integer institutionId;
    private Integer statusId;
    private Integer creatorId;
    private String nameUz;
    private String nameOz;
    private String nameRu;
    private String nameEn;
    private Integer beginYear;

    @ManyToOne(fetch = FetchType.LAZY)
    private University university;
}
