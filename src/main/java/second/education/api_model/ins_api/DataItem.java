package second.education.api_model.ins_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataItem{

	@JsonProperty("institution_id")
	private Integer institutionId;
	@JsonProperty("institution_name")
	private String institutionName;
	private String code;
	@JsonProperty("institution_type_id")
	private Integer institutionTypeId;
	@JsonProperty("institution_type_name")
	private String institutionTypeName;
	@JsonProperty("status_id")
	private Integer statusId;
	@JsonProperty("status_name")
	private String statusName;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("name_uz")
	private String nameUz;
	@JsonProperty("name_oz")
	private String nameOz;
	@JsonProperty("name_en")
	private String nameEn;
	@JsonProperty("name_ru")
	private String nameRu;
	@JsonProperty("opening_date")
	private String openingDate;
	@JsonProperty("termination_date")
	private String terminationDate;
	@JsonProperty("region_soato_id")
	private Integer regionSoatoId;
	@JsonProperty("region_name")
	private String regionName;
//	private Integer eduTypeId;
//	private String eduTypeName;
}
