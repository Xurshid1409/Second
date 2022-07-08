package second.education.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialitiesResponse {

    private Integer id;
    private Integer specialitiesId;
    private Integer code;
    private String createdAt;
    private Integer mehnatId;
    private Integer institutionId;
    private Integer statusId;
    private Integer creatorId;
    private String nameUz;
    private String nameOz;
    private String nameRu;
    private String nameEn;
    private Integer terminationYear;
    private Integer beginYear;
}
