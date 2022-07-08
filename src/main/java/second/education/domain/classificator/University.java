package second.education.domain.classificator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import second.education.domain.AbstractData;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class University extends AbstractData<Integer> {

    private Integer institutionId;
    private String institutionName;
    private Integer institutionTypeId;
    private String institutionTypeName;
    private String statusName;
    private String nameUz;
    private String nameOz;
    private String nameEn;
    private String nameRu;
    private Integer regionSoatoId;
    private String regionName;
}
