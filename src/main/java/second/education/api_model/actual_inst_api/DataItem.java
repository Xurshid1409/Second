package second.education.api_model.actual_inst_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataItem{

//	private String code;
//	private Integer institutionTypeId;
//	private String statusName;
//	private String createdAt;
//	private Integer statusId;
//	private Object terminationDate;
//	private Integer regionSoatoId;
	private Integer id;
	@JsonProperty("name_uz")
	private String nameUz;
	@JsonProperty("name_oz")
	private String nameOz;
	@JsonProperty("name_ru")
	private String nameRu;
	@JsonProperty("name_en")
	private String nameEn;
	@JsonProperty("region_name")
	private String regionName;
//	private String institutionTypeName;
//	private String openingDate;
//	private Integer eduTypeId;
//	private String eduTypeName;
}
