package second.education.api_model.spec_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataItem{

    private Integer id;
    private Integer code;
//    @JsonProperty("created_at")
//    private String createdAt;
//    @JsonProperty("mehnat_id")
//    private Integer mehnatId;
    @JsonProperty("institution_id")
    private Integer institutionId;
    @JsonProperty("status_id")
    private Integer statusId;
    @JsonProperty("creator_id")
    private Integer creatorId;
    @JsonProperty("name_uz")
    private String nameUz;
    @JsonProperty("name_oz")
    private String nameOz;
    @JsonProperty("name_ru")
    private String nameRu;
    @JsonProperty("name_en")
    private String nameEn;
//    @JsonProperty("termination_year")
//    private Integer terminationYear;
    @JsonProperty("begin_year")
    private Integer beginYear;
}
