package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponse {

    private Integer id;
    private Integer institutionId;
    private String institutionName;
    private Integer institutionTypeId;
    private String institutionTypeName;
    private String statusName;
    private String nameUz;
    private String nameOz;
    private String nameEn;
    private String nameRu;
    private String terminationDate;
    private Integer regionSoatoId;
    private String regionName;
}
