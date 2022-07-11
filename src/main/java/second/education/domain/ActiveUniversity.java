package second.education.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveUniversity extends AbstractData<Integer> {

    private Integer institutionId;
    private String nameUz;
    private String nameOz;
    private String nameRu;
    private String nameEn;
    private String regionName;
}
